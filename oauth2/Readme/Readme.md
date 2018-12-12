## OAuth2

The oauth2 library helps you to easily add an OAuth2 flow to your existing Android application.

It automatically shows a customizable Android dialog with WebView to guide the user to eventually grant you an access token.

To help you manage access tokens, the library also  stores tokens in SharedPreferences.

## Features

* Supports the following OAuth2 implicit authorization flows . 
* Manage access token, refresh token.
* Store access token and refresh token in SharedPreferences
* Allow access Access token and check expired status

## Installation

Copy and import spreadsheet in to your project like another library
[How to add external library](https://developer.android.com/studio/projects/android-library.html).

## Requirements

* Required minimum API level: 14
* Required INTERNET permission

## Usage
* You need config OAuth2Manager when init application, do it on onCreate application or activity.
```
// setup access token parameter
OAuth2Parameter accessTokenParam = new OAuth2Parameter(https://www.linkedin.com/uas/oauth2/accessToken)
        .addParameter("grant_type","authorization_code")
        .addParameter("client_id","your api key stay here")
        .addParameter("redirect_uri","https://local_app_deep_link/callback")
        .addParameter("client_secret","your secret key stay here");
// setup authorization parameter
OAuth2Parameter authorizationParam = new OAuth2Parameter(https://www.linkedin.com/uas/oauth2/authorization)
        .addParameter("response_type",code)
        .addParameter("client_id","your api key stay here")
        .addParameter("scope","r_basicprofile")
        .addParameter("state","9Jw5Ygjo4v06Swb") 
                .addParameter("redirect_uri","https://local_app_deep_link/callback");
OAuth2Parameter refreshTokenParam = new OAuth2Parameter(https://www.linkedin.com/uas/oauth2/refreshToken)   
       .addParameter("refresh_token","your refresh token stay here")
       .addParameter("grant_type","refresh_token")
OAuth2Config config = new OAuth2Config(authen,access,refreshTokenParam);
OAuth2Manager.getInstance().config(config);
```
*  Request access token; request to server and store it to local
```
        OAuth2Manager.getInstance().requestToken(Context, new OAuth2CallBack() {
            @Override
            public void callback(boolean success, String message) {
                // your result return here, check success variable to known request success or fail
            }
        });
```
* Get Access token stored at local
```
    String token = OAuth2Manager.getInstance().getToken(context);
```
* Check access token has expired or not
```
    boolean isExpired = OAuth2Manager.getInstance().isTokenExpired(context);
```
* Refresh token if your access token has expired
```
    OAuth2Manager.getInstance().refreshToken(Context, new OAuth2CallBack() {
        @Override
        public void callback(boolean success, String message) {
            // your result return here, check success variable to known request success or fail
        }
    });
```
* Clear all OAuth2 information on local, include access token, refresh token, expired time
```
    OAuth2Manager.getInstance().clearToken()
```
# History

Version: 1.0

* Initial version

## Authors

Developed by **Minh Pham** 