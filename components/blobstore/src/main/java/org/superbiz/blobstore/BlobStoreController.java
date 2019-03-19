package org.superbiz.blobstore;

import org.springframework.web.bind.annotation.*;
import org.superbiz.moviefun.blobstore.Blob;

import java.io.IOException;

@RestController
@RequestMapping("/blob")
public class BlobStoreController {
    private final BlobClient blobClient;

    public BlobStoreController(BlobClient blobClient) {
        this.blobClient = blobClient;
    }

    @PutMapping
    public void putBlob(@RequestBody Blob blob){
        blobClient.putBlob(blob);
    }

    @GetMapping("/{name}")
    public Blob getBlob(@PathVariable String name) throws IOException {
        return blobClient.getBlob(name);
    }
}
