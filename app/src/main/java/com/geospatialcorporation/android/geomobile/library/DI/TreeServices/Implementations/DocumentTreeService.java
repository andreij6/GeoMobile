package com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Implementations;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.IFullDataRepository;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestCallback;
import com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations.DocumentModifiedListener;
import com.geospatialcorporation.android.geomobile.library.rest.DocumentService;
import com.geospatialcorporation.android.geomobile.library.rest.DownloadService;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.Document.MoveRequest;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.RenameRequest;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class DocumentTreeService implements IDocumentTreeService {

    DocumentService Service;
    IFullDataRepository<Document> DocumentRepo;

    public DocumentTreeService(IFullDataRepository<Document> repository){
        Service = application.getRestAdapter().create(DocumentService.class);
        DocumentRepo = repository;
    }

    @Override
    public void delete(final int documentId) {
        Service.delete(documentId, new Callback<Document>() {
            @Override
            public void success(Document document, Response response) {
                DocumentRepo.Remove(documentId);

                Toast.makeText(application.getAppContext(), "Document Deleted", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void sendDocument(Folder currentFolder, Uri data) {
        TypedFile typedFile = new TypedFile("multipart/form-data", new File(data.getPath()));
        Service.create(currentFolder.getId(), typedFile, new RequestCallback<>(new DocumentModifiedListener()));
    }

    @Override
    public void sendTakenImage(Folder currentFolder, Uri data) {
        final TypedFile t = new TypedFile("image/jpg", new File(data.getPath()));
        final String path = data.getPath();

        SendImage(currentFolder, t, path);
    }

    @Override
    public void sendPickedImage(Folder currentFolder, Uri data) {
        String actualPath = getRealPathFromUri(data);
        String mimeType = getMimeTypeOfFile(actualPath);

        final TypedFile t = new TypedFile(mimeType, new File(actualPath));

        SendImage(currentFolder, t, actualPath);
    }

    @Override
    public void download(int documentId, String documentName) {
        //region Attempt to download file using Retrofit
        /*
        String dir = Environment.DIRECTORY_DOWNLOADS;
        dir += "/geomobile";
        final File fileDir = new File(dir);
        if (!fileDir.isDirectory()) {
            fileDir.mkdir();
        }

        Service.download(documentId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                byte[] fileBytes = ((TypedByteArray)response.getBody()).getBytes();

                try (FileOutputStream output = new FileOutputStream(fileDir)) {
                    output.write(fileBytes);

                    Toast.makeText(application.getAppContext(), "Sucess", Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    Toast.makeText(application.getAppContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (IOException e) {
                    Toast.makeText(application.getAppContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(application.getAppContext(), error.getMessage(), Toast.LENGTH_LONG).show();


            }
        });
        **/
        //endregion

        new DownloadService(documentId, documentName);
    }

    @Override
    public void rename(int documentId, String documentName) {
        if(AuthorizedToRenameDocument(documentId)) {
            Service.rename(documentId, new RenameRequest(documentName), new RequestCallback<>(new DocumentModifiedListener()));

            Document doc = DocumentRepo.getById(documentId);
            doc.setName(documentName);
            DocumentRepo.update(doc, documentId);
        } else {
            Toast.makeText(application.getAppContext(), "Not Authorized to Rename Layer", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void move(Document document, int folderId) {
        MoveRequest moveRequest = new MoveRequest(document, folderId);

        Service.moveDocument(document.getId(), moveRequest, new RequestCallback<>(new DocumentModifiedListener(false)));

    }

    //region Helpers
    protected Boolean AuthorizedToRenameDocument(int id){
        //Not sure if there are restrictions on this but if so we check here
        return true;
    }

    protected void SendImage(Folder currentFolder, final TypedFile t, final String path) {
        Service.create(currentFolder.getId(), t, new RequestCallback<>(new DocumentModifiedListener()));
    }

    protected String getMimeTypeOfFile(String pathName) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, opt);
        return opt.outMimeType;
    }

    protected String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = application.getAppContext().getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    //endregion
}
