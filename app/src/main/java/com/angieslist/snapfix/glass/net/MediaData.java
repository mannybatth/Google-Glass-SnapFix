package com.angieslist.snapfix.glass.net;

/**
 * Created by sjs on 5/10/14.
 */
/**
 *       "id": 12345,
 "userId": 200,
 "serviceRequestId":6789,
 "fileName": "test.jpg",
 "validated": false,
 "s3Url": null,
 "putUrl": "https://servicetown-dev.s3.amazonaws.com/2/1/image/1-test.jpg?Expires=1376688895&AWSAccessKeyId=AKIAI4K7BO67GZY74KFA&Signature=EDyOjV3xkCD%2BzLetHIsyOcMuI4c%3D"
 "expiration": null,
 "createdAt": 1273849032
 * @author jyotsna
 *
 */
public class MediaData {

    public String getId() {
        return id;
    }
    public String getUserId() {
        return userId;
    }
    public String getServiceRequestId() {
        return serviceRequestId;
    }
    public String getFileName() {
        return fileName;
    }
    public boolean isValidated() {
        return validated;
    }
    public String getS3Url() {
        return s3Url;
    }
    public String getPutUrl() {
        return putUrl;
    }
    public long getExpiration() {
        return expiration;
    }
    public long getCreatedAt() {
        return createdAt;
    }
    private String id;
    private String userId;
    private String serviceRequestId;
    private String fileName;
    private boolean validated;
    private String s3Url;
    private String putUrl;
    private long expiration;
    private long createdAt;
}