package org.superbiz.blobstore;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.apache.tika.Tika;
import org.springframework.web.client.RestOperations;
import org.superbiz.moviefun.blobstore.Blob;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

public class BlobClient {
    private String blobUrl;
    private RestOperations restOperations;
    private  AmazonS3 s3=null;
    private  String bucketName=null;
    private final Tika tika = new Tika();

    public BlobClient(String blobUrl, RestOperations restOperations) {
        this.blobUrl = blobUrl;
        this.restOperations = restOperations;
    }

    public BlobClient(String blobUrl, RestOperations restOperations, AmazonS3 s3, String bucketName) {
        this.blobUrl = blobUrl;
        this.restOperations = restOperations;
        this.s3 = s3;
        this.bucketName = bucketName;
    }

    public void putBlob(Blob blob){
        s3.putObject(bucketName, blob.name, blob.inputStream, new ObjectMetadata());
        restOperations.postForEntity(blobUrl,blob,Blob.class);
    }

    public Blob getBlob(String name) throws IOException {
        if (!s3.doesObjectExist(bucketName, name)) {
            return null;
        }

        S3Object s3Object = s3.getObject(bucketName, name);
        S3ObjectInputStream content = s3Object.getObjectContent();

        byte[] bytes = IOUtils.toByteArray(content);

//        return Optional.of(new Blob(
//                name,
//                new ByteArrayInputStream(bytes),
//                tika.detect(bytes)
//        ));
        return restOperations.getForEntity(blobUrl + "/" + name,Blob.class).getBody();
    }

}
