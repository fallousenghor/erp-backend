# TODO: Fix "Cannot read properties of undefined (reading 'payload')" Error

## Problem
The error `TypeError: Cannot read properties of undefined (reading 'payload')` occurs in Axios when the frontend services use non-null assertions (`!`) on `response.data.data` without checking if the response structure is valid.

## Root Cause
Frontend services use `response.data.data!` (non-null assertion) without checking:
1. If `response.data` exists
2. If `response.data.data` exists
3. If the API returns an error response (where `data` might be undefined)

## Fix Plan

### Step 1: Fix merchant.service.ts
- [x] Add helper function to safely extract data from responses
- [x] Add null checks before using non-null assertions
- [x] Handle cases where API returns error responses

### Step 2: Fix auth.service.ts
- [x] Same approach as merchant.service.ts
- [x] Add safe data extraction

### Step 3: Fix other service files
- [x] Fixed businessCard.service.ts
- [x] Fixed stats.service.ts

## Status
- [x] Step 1: Fix merchant.service.ts
- [x] Step 2: Fix auth.service.ts
- [x] Step 3: Check and fix other service files

## Changes Made
1. Added `getResponseData<T>()` helper function to all service files
2. Replaced all `response.data.data!` calls with `getResponseData(response)`
3. The helper function now properly validates:
   - If `response.data` exists
   - If the API response indicates success
   - If `response.data.data` is not null/undefined

## Additional Fix: Add CV Display

### Issue
User reported not seeing the CV button in the frontend.

### Solution
- Added CV display in Merchant details page (Merchantdetails.tsx)
- Added CV column in merchants list (Merchantslist.tsx)

### Files Modified
1. **frontend/src/pages/Merchantdetails.tsx**:
   - Added "CV" section with "Voir le CV" and "Télécharger" buttons
   - Added FilePdfOutlined and DownloadOutlined icons

2. **frontend/src/pages/Merchantslist.tsx**:
   - Added "CV" column showing "Disponible" or "Aucun" status
   - Added FilePdfOutlined icon import

## Build Status
✅ Build successful

