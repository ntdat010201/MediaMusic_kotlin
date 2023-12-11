package com.example.mediamusickotlin.model

data class Music(
    val id: String,
    val title: String,
    val album: String,
    val artist: String,
    val duration: Long,
    val path: String,
)

class Playlist {
    lateinit var name: String
    lateinit var playlist: ArrayList<Music>
    lateinit var createdBy : String
    lateinit var createdOn : String
}

class MusicPlaylist{
    var ref :ArrayList<Playlist> = ArrayList()
}


