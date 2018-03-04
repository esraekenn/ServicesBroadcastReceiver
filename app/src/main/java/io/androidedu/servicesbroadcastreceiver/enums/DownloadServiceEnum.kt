package io.androidedu.servicesbroadcastreceiver.enums


/******************************
 * Created by Gökhan ÖZTÜRK   |
 * 4.03.2018                 |
 * GokhanOzturk@AndroidEdu.IO |
 *****************************/

enum class DownloadServiceEnum {

    URL {
        override fun toString(): String {
            return "urlpath"
        }
    },
    FILENAME {
        override fun toString(): String {
            return "filename"
        }
    },

    FILEPATH {
        override fun toString(): String {
            return "filepath"
        }
    },

    RESULT {
        override fun toString(): String {
            return "RESULT"
        }
    },
    NOTIFICATION {

        override fun toString(): String {
            return "com.androidedu.cheatedbabe.service.DownloadDataService"
        }
    }
}