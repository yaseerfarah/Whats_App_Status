package yaseerfarah22.com.whatsappsticker_2.Data;

import android.Manifest;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import yaseerfarah22.com.whatsappsticker_2.Adapter.HomeCardViewAdapter;
import yaseerfarah22.com.whatsappsticker_2.BuildConfig;
import yaseerfarah22.com.whatsappsticker_2.POJO.Sticker;
import yaseerfarah22.com.whatsappsticker_2.POJO.StickerPack;
import yaseerfarah22.com.whatsappsticker_2.POJO.StickerPackCollections;
import yaseerfarah22.com.whatsappsticker_2.Util.ContentFileParser;

import static android.os.ParcelFileDescriptor.MODE_READ_ONLY;

import static yaseerfarah22.com.whatsappsticker_2.Data.ReadAndWriteToFile.FILE_NAME;


public class StickerContentProvider extends ContentProvider {

    public static final String STICKER_PACK_IDENTIFIER_IN_QUERY = "sticker_pack_identifier";
    public static final String STICKER_PACK_NAME_IN_QUERY = "sticker_pack_name";
    public static final String STICKER_PACK_PUBLISHER_IN_QUERY = "sticker_pack_publisher";
    public static final String STICKER_PACK_ICON_IN_QUERY = "sticker_pack_icon";
    public static final String ANDROID_APP_DOWNLOAD_LINK_IN_QUERY = "android_play_store_link";
    public static final String IOS_APP_DOWNLOAD_LINK_IN_QUERY = "ios_app_download_link";
    public static final String PUBLISHER_EMAIL = "sticker_pack_publisher_email";
    public static final String PUBLISHER_WEBSITE = "sticker_pack_publisher_website";
    public static final String PRIVACY_POLICY_WEBSITE = "sticker_pack_privacy_policy_website";
    public static final String LICENSE_AGREENMENT_WEBSITE = "sticker_pack_license_agreement_website";
    public static final String IMAGE_DATA_VERSION = "image_data_version";
    public static final String AVOID_CACHE = "whatsapp_will_not_cache_stickers";


    public static final String STICKER_FILE_NAME_IN_QUERY = "sticker_file_name";
    public static final String STICKER_FILE_EMOJI_IN_QUERY = "sticker_emoji";
    private static final String CONTENT_FILE_NAME = "contents.json";

    private static  UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static final String METADATA = "metadata";
    private static final int METADATA_CODE = 1;
    private static final int METADATA_CODE_FOR_SINGLE_PACK = 2;
    private static final int STICKERS_CODE = 3;
    static final String STICKERS_ASSET = "stickers_asset";
    private static final int STICKERS_ASSET_CODE = 4;
    private static final int STICKER_PACK_TRAY_ICON_CODE = 5;
    static final String STICKERS = "stickers";



    static final String URL = "content://" + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "/onstart";
    public static final Uri CONTENT_URI = Uri.parse(URL);


    private List<StickerPack> stickerPackList;
    private List<StickerPackCollections> stickerPackCollections=new ArrayList<>();
    private String[] reqPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
            .READ_EXTERNAL_STORAGE};

    private ReadAndWriteToFile readAndWriteToFile;

    @Override
    public boolean onCreate() {
        readAndWriteToFile=new ReadAndWriteToFile(Objects.requireNonNull(getContext()));
        onStart();

        return true;
    }


    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {

        if (method.trim().matches("on_create")){

            onStart();
        }

        return null;
    }

    private void onStart(){

        final String authority = BuildConfig.CONTENT_PROVIDER_AUTHORITY;
        if (!authority.startsWith(Objects.requireNonNull(getContext()).getPackageName())) {
            throw new IllegalStateException("your authority (" + authority + ") for the content provider should start with your package name: " + getContext().getPackageName());
        }
        MATCHER=new UriMatcher(UriMatcher.NO_MATCH);
        MATCHER.addURI(authority, METADATA, METADATA_CODE);
        MATCHER.addURI(authority, METADATA + "/*", METADATA_CODE_FOR_SINGLE_PACK);
        MATCHER.addURI(authority, STICKERS + "/*", STICKERS_CODE);

        for (StickerPack stickerPack : getStickerPackList()) {
            MATCHER.addURI(authority, STICKERS_ASSET + "/" + stickerPack.identifier + "/" + stickerPack.trayImageFile, STICKER_PACK_TRAY_ICON_CODE);
            for (Sticker sticker : stickerPack.getStickers()) {
                MATCHER.addURI(authority, STICKERS_ASSET + "/" + stickerPack.identifier + "/" + sticker.imageFileName, STICKERS_ASSET_CODE);
            }
        }



    }




    private List<StickerPack> getStickerPackList() {

            readContentFile(Objects.requireNonNull(getContext()));

        return stickerPackList;
    }



    private synchronized void readContentFile(@NonNull Context context) {


        boolean permissionCheck1 = ContextCompat.checkSelfPermission(getContext(), reqPermissions[0]) ==
                PackageManager.PERMISSION_GRANTED;
        boolean permissionCheck2 = ContextCompat.checkSelfPermission(getContext(), reqPermissions[1]) ==
                PackageManager.PERMISSION_GRANTED;





        if (permissionCheck1 && permissionCheck2){
            File file=new File(readAndWriteToFile.getStickerFile(), FILE_NAME);
            if (file.exists()) {


                try (InputStream contentsInputStream = new FileInputStream(new File(readAndWriteToFile.getStickerFile(), FILE_NAME))) {
                    stickerPackList = ContentFileParser.parseStickerPacks(contentsInputStream);
                } catch (IOException | IllegalStateException e) {
                    throw new RuntimeException(CONTENT_FILE_NAME + " file has some issues: " + e.getMessage(), e);
                }
            }else {
                stickerPackList=new ArrayList<>();
            }

        }else {
            stickerPackList=new ArrayList<>();

            // If permissions are not already granted, request permission from the user.
        }







    }


    @Override
    public String getType(@NonNull Uri uri) {
        final int matchCode = MATCHER.match(uri);
        switch (matchCode) {
            case METADATA_CODE:
                return "vnd.android.cursor.dir/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + METADATA;
            case METADATA_CODE_FOR_SINGLE_PACK:
                return "vnd.android.cursor.item/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + METADATA;
            case STICKERS_CODE:
                return "vnd.android.cursor.dir/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + STICKERS;
            case STICKERS_ASSET_CODE:
                return "image/webp";
            case STICKER_PACK_TRAY_ICON_CODE:
                return "image/png";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        onStart();
        return uri;

    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not supported");
    }


    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == METADATA_CODE) {
            return getPackForAllStickerPacks(uri);
        } else if (code == METADATA_CODE_FOR_SINGLE_PACK) {
            return getCursorForSingleStickerPack(uri);
        } else if (code == STICKERS_CODE) {
            return getStickersForAStickerPack(uri);
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }



    private Cursor getPackForAllStickerPacks(@NonNull Uri uri) {
        return getStickerPackInfo(uri, getStickerPackList());
    }

    private Cursor getCursorForSingleStickerPack(@NonNull Uri uri) {
        final String identifier = uri.getLastPathSegment();
        for (StickerPack stickerPack : getStickerPackList()) {
            if (identifier.equals(stickerPack.identifier)) {
                return getStickerPackInfo(uri, Collections.singletonList(stickerPack));
            }
        }

        return getStickerPackInfo(uri,new ArrayList<StickerPack>());
    }

    @NonNull
    private Cursor getStickerPackInfo(@NonNull Uri uri, @NonNull List<StickerPack> stickerPackList) {
        MatrixCursor cursor = new MatrixCursor(
                new String[]{
                        STICKER_PACK_IDENTIFIER_IN_QUERY,
                        STICKER_PACK_NAME_IN_QUERY,
                        STICKER_PACK_PUBLISHER_IN_QUERY,
                        STICKER_PACK_ICON_IN_QUERY,
                        ANDROID_APP_DOWNLOAD_LINK_IN_QUERY,
                        IOS_APP_DOWNLOAD_LINK_IN_QUERY,
                        PUBLISHER_EMAIL,
                        PUBLISHER_WEBSITE,
                        PRIVACY_POLICY_WEBSITE,
                        LICENSE_AGREENMENT_WEBSITE,
                        IMAGE_DATA_VERSION,
                        AVOID_CACHE,
                });
        for (StickerPack stickerPack : stickerPackList) {
            MatrixCursor.RowBuilder builder = cursor.newRow();
            builder.add(stickerPack.identifier);
            builder.add(stickerPack.name);
            builder.add(stickerPack.publisher);
            builder.add(stickerPack.trayImageFile);
            builder.add("");
            builder.add("");
            builder.add(stickerPack.publisherEmail);
            builder.add(stickerPack.publisherWebsite);
            builder.add(stickerPack.privacyPolicyWebsite);
            builder.add(stickerPack.licenseAgreementWebsite);
            builder.add(stickerPack.imageDataVersion);
            builder.add(stickerPack.avoidCache ? 1 : 0);
        }
        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }

    @NonNull
    private Cursor getStickersForAStickerPack(@NonNull Uri uri) {
        final String identifier = uri.getLastPathSegment();
        MatrixCursor cursor = new MatrixCursor(new String[]{STICKER_FILE_NAME_IN_QUERY, STICKER_FILE_EMOJI_IN_QUERY});
        for (StickerPack stickerPack : getStickerPackList()) {
            if (identifier.equals(stickerPack.identifier)) {
                for (Sticker sticker : stickerPack.getStickers()) {
                    cursor.addRow(new Object[]{sticker.imageFileName, TextUtils.join(",", sticker.emojis)});
                }
            }
        }
        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }



    @Nullable
    @Override
    public AssetFileDescriptor openAssetFile(@NonNull Uri uri, @NonNull String mode) {
        final int matchCode = MATCHER.match(uri);
        if (matchCode == STICKERS_ASSET_CODE || matchCode == STICKER_PACK_TRAY_ICON_CODE) {
//            Toast.makeText(Objects.requireNonNull(getContext()),"Image",Toast.LENGTH_LONG).show();

            return getImage(uri);
        }
        return null;
    }







    private AssetFileDescriptor getImage(Uri uri) throws IllegalArgumentException {
        Log.w("IMAGE ASSET", "ASKING FOR ASSET?");
        final List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() != 3) {
            throw new IllegalArgumentException("path segments should be 3, uri is: " + uri);
        }
        String fileName = pathSegments.get(pathSegments.size() - 1);
        final String identifier = pathSegments.get(pathSegments.size() - 2);
        if (TextUtils.isEmpty(identifier)) {
            throw new IllegalArgumentException("identifier is empty, uri: " + uri);
        }
        if (TextUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("file name is empty, uri: " + uri);
        }
        //making sure the file that is trying to be fetched is in the list of stickers.
        for (StickerPack stickerPack : getStickerPackList()) {
            if (identifier.equals(stickerPack.identifier)) {
                if (fileName.equals(stickerPack.trayImageFile)) {
                    Uri uri1=Uri.fromFile(new File(stickerPack.trayImageFile));
                    File imageDir=new File(readAndWriteToFile.getStickerFile(),identifier);
                    File file=new File(imageDir,stickerPack.trayImageFile);
                    try {
                        return new AssetFileDescriptor(ParcelFileDescriptor.open(file, MODE_READ_ONLY),0,file.length()) ;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    for (Sticker sticker : stickerPack.getStickers()) {
                        if (fileName.equals(sticker.imageFileName)) {
                            Uri uri1=Uri.fromFile(new File(sticker.imageFileName));
                            File imageDir=new File(readAndWriteToFile.getStickerFile(),identifier);
                            File file=new File(imageDir,sticker.imageFileName);
                            try {
                                return new AssetFileDescriptor(ParcelFileDescriptor.open(file, MODE_READ_ONLY),0,file.length()) ;
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }





    private AssetFileDescriptor getImageAsset(Uri uri) throws IllegalArgumentException {
        Log.w("IMAGE ASSET", "ASKING FOR ASSET?");
        AssetManager am = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am = Objects.requireNonNull(getContext()).getAssets();
        }
        final List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() != 3) {
            throw new IllegalArgumentException("path segments should be 3, uri is: " + uri);
        }
        String fileName = pathSegments.get(pathSegments.size() - 1);
        final String identifier = pathSegments.get(pathSegments.size() - 2);
        if (TextUtils.isEmpty(identifier)) {
            throw new IllegalArgumentException("identifier is empty, uri: " + uri);
        }
        if (TextUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("file name is empty, uri: " + uri);
        }
        //making sure the file that is trying to be fetched is in the list of stickers.
        for (StickerPack stickerPack : getStickerPackList()) {
            if (identifier.equals(stickerPack.identifier)) {
                if (fileName.equals(stickerPack.trayImageFile)) {
                    return fetchFile(uri, am, fileName, identifier);
                } else {
                    for (Sticker sticker : stickerPack.getStickers()) {
                        if (fileName.equals(sticker.imageFileName)) {
                            return fetchFile(uri, am, fileName, identifier);
                        }
                    }
                }
            }
        }
        return null;
    }







    private AssetFileDescriptor fetchFile(@NonNull Uri uri, @NonNull AssetManager am, @NonNull String fileName, @NonNull String identifier) {
        try {
            return am.openFd(identifier + "/" + fileName);
        } catch (IOException e) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Log.e(Objects.requireNonNull(getContext()).getPackageName(), "IOException when getting asset file, uri:" + uri, e);
            }
            return null;
        }
    }


}
