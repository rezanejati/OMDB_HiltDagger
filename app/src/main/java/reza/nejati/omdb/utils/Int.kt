package reza.nejati.omdb.utils

import reza.nejati.omdb.def.Const

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

fun Int.getSearchType(): String {
    return if (this == 0) Const.MOVIE else Const.SERIES
}


