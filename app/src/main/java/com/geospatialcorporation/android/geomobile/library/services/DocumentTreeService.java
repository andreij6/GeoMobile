package com.geospatialcorporation.android.geomobile.library.services;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.IFullDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Documents.DocumentsAppSource;
import com.geospatialcorporation.android.geomobile.library.helpers.Interfaces.ITreeService;
import com.geospatialcorporation.android.geomobile.library.rest.DocumentService;
import com.geospatialcorporation.android.geomobile.library.rest.DownloadService;
import com.geospatialcorporation.android.geomobile.models.Document.MoveRequest;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.Document.DocumentCreateResponse;
import com.geospatialcorporation.android.geomobile.models.RenameRequest;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by andre on 6/2/2015.
 */
public class DocumentTreeService implements ITreeService {

    //region Properties
    DocumentService Service;
    IFullDataRepository<Document> DocumentRepo;
    //endregion

    public DocumentTreeService(){
        Service = application.getRestAdapter().create(DocumentService.class);
        DocumentRepo = new DocumentsAppSource();
    }

    //region Public Methods
    public void delete(Document document) {
        final int id = document.getId();
        Service.delete(document.getId(), new Callback<Document>() {
            @Override
            public void success(Document document, Response response) {
                DocumentRepo.Remove(id);

                Toast.makeText(application.getAppContext(), "Document Deleted", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }

    public void SendDocument(Folder currentFolder, Uri data) {
        TypedFile typedFile = new TypedFile("multipart/form-data", new File(data.getPath()));
        Service.create(currentFolder.getId(), typedFile, new Callback<DocumentCreateResponse>() {
            @Override
            public void success(DocumentCreateResponse documentCreateResponse, Response response) {

                Toast.makeText(application.getAppContext(), "Success!", Toast.LENGTH_SHORT).show();
                Toast.makeText(application.getAppContext(), "Pull Down Refresh", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(application.getAppContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SendTakenImage(Folder currentFolder, Uri data) {
        final TypedFile t = new TypedFile("image/jpg", new File(data.getPath()));
        final String path = data.getPath();

        SendImage(currentFolder, t, path);
    }

    public void SendPickedImage(Folder currentFolder, Uri data) {
        String actualPath = getRealPathFromUri(data);
        String mimeType = getMimeTypeOfFile(actualPath);

        final TypedFile t = new TypedFile(mimeType, new File(actualPath));

        SendImage(currentFolder, t, actualPath);
    }

    public void download(int documentId, String docName){
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

        new DownloadService(documentId, docName);
    }

    public Boolean rename(final int documentId, final String docName){
        if(!AuthorizedToRenameDocument(documentId)) return false;

        Service.rename(documentId, new RenameRequest(docName), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Document l = DocumentRepo.getById(documentId);
                if(l != null){
                    l.setName(docName);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        Document doc = DocumentRepo.getById(documentId);
        doc.setName(docName);

        return true;


    }

    public void move(Document document, int toFolderId){
        MoveRequest moveRequest = new MoveRequest(document, toFolderId);

        Service.moveDocument(document.getId(), moveRequest, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d("DocumentTreeService", "Success");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("DocumentTreeService", "Failure. " + error.getMessage());

            }
        });
    }
    //endregion

    //region Helpers
    protected Boolean AuthorizedToRenameDocument(int id){
        //Not sure if there are restrictions on this but if so we check here
        return true;
    }

    protected void SendImage(Folder currentFolder, final TypedFile t, final String path) {
        Service.create(currentFolder.getId(), t, new Callback<DocumentCreateResponse>() {
            @Override
            public void success(DocumentCreateResponse documentCreateResponse, Response response) {

                Toast.makeText(application.getAppContext(), "Success!", Toast.LENGTH_SHORT).show();
                Toast.makeText(application.getAppContext(), "Pull Down Refresh", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(application.getAppContext(), "Name: " + t.fileName(), Toast.LENGTH_LONG).show();
                Toast.makeText(application.getAppContext(), "Path: " + path, Toast.LENGTH_LONG).show();
                Toast.makeText(application.getAppContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
