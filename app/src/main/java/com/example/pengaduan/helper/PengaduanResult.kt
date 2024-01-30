package com.example.pengaduan.helper

data class PengaduanResult(
    var id: Int? = null,
    var nim: String? = null,
    var isiPengaduan: String? = null,
    var foto: String? = null,
    var isActive: Int? = null,
    var isArchive: Int? = null,
    var userId: Int? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null
)
