package com.ghazalpaknia_mahdinazarian.app_models

import com.ghazalpaknia_mahdinazarian.static_values.StaticValues;

class DownloadsItemModel (
    var itemName : String,
    var downloadState : StaticValues.DownloadStates,
    var downloadProgress : Int,
    var downloadSpeed : Long,
    var downloadFileSize : Long,
    var downloadedFileSize : Long
) {
}