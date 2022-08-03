signingConfigs {
        release {
            storeFile file('./../releasekey/firebox_keystore.jks')
            keyAlias 'haier'
            keyPassword 'android'
            storePassword 'android'
        }
        debug {
            storeFile file('./../releasekey/firebox_keystore.jks')
            keyAlias 'haier'
            keyPassword 'android'
            storePassword 'android'
        }
        //TODO harmonybufen
//        release {
//            storeFile file('./../releasekey/harmonyandroid.jks')
//            keyAlias 'harmonyandroid'
//            keyPassword 'HarmonyAndroid'
//            storePassword 'HarmonyAndroid'
//        }
//        debug {
//            storeFile file('./../releasekey/harmonyandroid.jks')
//            keyAlias 'harmonyandroid'
//            keyPassword 'HarmonyAndroid'
//            storePassword 'HarmonyAndroid'
//        }
    }