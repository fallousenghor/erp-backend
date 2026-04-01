# Cloudinary Upload Issue - Troubleshooting Guide

## Current Status

### ✅ Fixed Issues
- **Backend API**: Returns correct image URLs in format `https://res.cloudinary.com/dvr66cxgj/image/upload/photos/[uuid].[ext]`
- **URL Generation**: No longer generates malformed `v1/photos/photos/...` URLs
- **Angular Frontend**: Enhanced with proper CORS handling and image error handling
- **Database**: Correctly stores image metadata and URLs

### ❌ Outstanding Issue
**Files uploaded to Cloudinary are not actually stored** - resulting in 404 when accessing them.

**Evidence:**
```
URL: https://res.cloudinary.com/dvr66cxgj/image/upload/photos/d42f1557-383c-447f-8b11-a36fcd3b99d1.jpeg
Response: HTTP 404
Error: x-cld-error: Resource not found - photos/d42f1557-383c-447f-8b11-a36fcd3b99d1
```

## Root Cause Analysis

The Cloudinary SDK is receiving the upload request and returning a success response with a `public_id`, but the actual file content is NOT being stored in the Cloudinary account.

### Possible Causes:
1. **Invalid/Expired API Credentials**
   - API Key: `349651835465745`
   - API Secret: `MPUQYSBGBbj0o0MTb8zp5-tRqf0`
   - Cloud Name: `dvr66cxgj`

2. **Account Permissions**
   - API key may not have "upload" permission
   - Could be restricted to specific folders/transformations

3. **Account Status**
   - Free tier quota exceeded
   - Account disabled or suspended
   - Billing issue

4. **Network/Configuration**
   - Firewall blocking Cloudinary uploads
   - Timeout during upload
   - SSL certificate verification failure

## Solution Steps

### Step 1: Verify Cloudinary Account Credentials
```bash
# Login to your Cloudinary dashboard at:
https://cloudinary.com/console

# Verify:
- Cloud Name: dvr66cxgj
- API Key: 349651835465745
- Account Status: Active
- Upload Preset: Verify if required
```

### Step 2: Test API Directly
```bash
# Test upload using curl (replace {file_path} with actual file):
curl -X POST \
  https://api.cloudinary.com/v1_1/dvr66cxgj/image/upload \
  -F "file=@{file_path}" \
  -F "api_key=349651835465745"

# Should return JSON with:
# - "public_id": "photos/[uuid].[ext]"
# - "secure_url": "https://res.cloudinary.com/dvr66cxgj/image/upload/photos/[uuid].[ext]"
```

### Step 3: Check API Key Permissions
1. Go to https://cloudinary.com/console/api-keys
2. Click on the API key being used
3. Verify it has "upload" permission
4. If not, regenerate or create a new key with upload permission

### Step 4: Verify Account Settings
```
Dashboard → Settings → Upload:
- Unsigned uploads: Enabled/Disabled?
- Upload presets required?
- Folder structure: Is "photos/" folder allowed?
```

### Step 5: Update Backend Configuration (if credentials changed)
**File**: `src/main/java/com/ceremonie/demo/config/CloudinaryConfig.java`

```java
@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "YOUR_CLOUD_NAME",      // Update
            "api_key", "YOUR_API_KEY",             // Update
            "api_secret", "YOUR_API_SECRET"        // Update
        ));
    }
}
```

### Step 6: Rebuild and Test
```bash
cd /home/gallas/Bureau/td/demo
mvn clean spring-boot:run -DskipTests

# Test upload via Swagger UI or frontend
# Check server logs for debug messages:
# "Starting file upload: ..."
# "Upload response - publicId=..., secure_url=..."
```

## Debugging Information

### Server Logging
The application now logs detailed upload information:
```
Starting file upload: originalFilename={file}, directory=photos, fileSize={size} bytes
File bytes length: {size}
Uploading to Cloudinary with publicId=photos/{uuid}.[ext], fileSize={size}
Upload response - publicId={value}, secure_url={url}, url={url}, error={error}
Full upload response: {response_map}
```

### Check Upload Logs
```bash
# View recent server logs:
tail -f /tmp/server.log | grep -i "upload"
```

## Alternative: Use Cloudinary Upload Widget

If the API approach continues to fail, consider using Cloudinary's hosted widget:

**Frontend Implementation**:
```typescript
// Add CldUploadWidget from cloudinary-react/next
// This shifts upload responsibility to Cloudinary's secure servers
```

## Backend Code Changes Made

### FileStorageServiceImpl.java Improvements:
- ✅ Added detailed logging for uploads
- ✅ Added file size validation
- ✅ Added error response checking
- ✅ Improved exception handling
- ✅ URL construction fixed (no v1 prefix)

### Angular Frontend Improvements:
- ✅ Created ImageLoaderService for CORS handling
- ✅ Added error handling for failed image loads
- ✅ Added fallback thumbnail URLs
- ✅ Added proper security context with `crossorigin="anonymous"`
- ✅ Added lazy loading for images

## Contact Cloudinary Support

If unable to resolve:
1. Visit: https://cloudinary.com/console
2. Support → Submit Issue
3. Provide:
   - Cloud name: `dvr66cxgj`
   - Error details from logs
   - Example failing file name and size
   - When uploads last worked (if applicable)

## Testing File Upload

**Manual Test Command**:
```bash
# Create test image
echo "test" > /tmp/test.txt

# Upload via backend
curl -X POST http://localhost:8086/api/files/upload \
  -H "Authorization: Bearer {token}" \
  -F "file=@/tmp/test.txt" \
  -F "type=PHOTO"

# Check response for file URL
# Verify file exists in Cloudinary: https://cloudinary.com/console/media_library
```

## Next Steps

1. **Verify API credentials** in Cloudinary console
2. **Test API directly** with curl command above
3. **Check account permissions** and account status
4. **Run upload test** from Angular frontend
5. **Review server logs** for any error messages
6. **Contact Cloudinary support** if credentials are correct but still failing
