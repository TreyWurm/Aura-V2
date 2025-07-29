package at.nukular.aura

import android.content.Context
import android.os.storage.StorageManager
import androidx.core.text.isDigitsOnly
import at.nukular.core.extensions.size
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton


private const val DIR_NAME = "logs"
private const val MAX_LOG_DIR_SIZE = 1_000_000L * 2 // 2MB == ~9k log entries
private const val JSON_FILE_NAME = "export.json"
private const val CURRENT_FILE_NAME = "current"
private const val BACKUP_FILE_NAME = "current.backup"

@Singleton
class FileWriterReader @Inject constructor(
    @param:ApplicationContext val context: Context,
) {

    private var cacheSpaceQuota: Long = MAX_LOG_DIR_SIZE
    private val dataDir: File = File(context.filesDir, DIR_NAME)
    private var entries = mutableMapOf<Tischler, MutableList<LocalDateTime>>()


    init {
        (context.getSystemService(Context.STORAGE_SERVICE) as? StorageManager)?.let { storageManager ->
            try {
                cacheSpaceQuota = storageManager.getCacheQuotaBytes(storageManager.getUuidForPath(context.cacheDir))
            } catch (_: Exception) {
            }
        }

        if (!dataDir.exists() && !dataDir.mkdirs()) {
            Timber.e("Failed to make disk-data dir $dataDir")
        }

        entries = readEntriesFromFile()
    }

    fun addEntry(date: LocalDateTime, tischler: Tischler) {
        val entry = entries[tischler]
        when (entry) {
            null -> entries.put(tischler, mutableListOf(date))
            else -> entries.put(tischler, (entry + date).toMutableList())
        }

        writeEntryToFile(date, tischler)
    }

    fun writeEntryToFile(date: LocalDateTime, tischler: Tischler) {
        val newFile = File(dataDir, CURRENT_FILE_NAME)
        var dataWritten = false

        FileOutputStream(newFile, true).bufferedWriter().use { writer ->
            try {
                writer.appendLine("${tischler.string},$date")
                dataWritten = true
            } catch (e: Exception) {
                Timber.w("Error writing Entry to file. $e")
            }
        }

        if (!dataWritten) {
            newFile.delete()
        }
    }

    fun writeEntriesToFile(tischler: Tischler): File {
        val newFile = File(dataDir, JSON_FILE_NAME)

        val json = JSONObject()
        json.put("submitted_by", tischler.string)
        val array = JSONArray()
        entries.forEach { entry ->
            val tischler = JSONObject()
            tischler.put("name", entry.key.string)
            tischler.put("times", JSONArray(entry.value.map { dateTime ->
                JSONObject()
                    .put("date", DateTimeFormatter.ISO_LOCAL_DATE.format(dateTime))
                    .put("time", DateTimeFormatter.ofPattern("HH:mm").format(dateTime))
            }))
            array.put(tischler)
        }
        json.put("Stammtischler", array)

        newFile.bufferedWriter().use { writer ->
            writer.write(json.toString())
        }

        return newFile
    }

    fun readEntriesFromFile(): MutableMap<Tischler, MutableList<LocalDateTime>> {
        val currentFile = File(dataDir, CURRENT_FILE_NAME)
        val backupFile = File(dataDir, BACKUP_FILE_NAME)
        val file = when {
            currentFile.exists() && currentFile.isFile -> currentFile
            backupFile.exists() && backupFile.isFile -> backupFile
            else -> return mutableMapOf()
        }

        file.bufferedReader().use { reader ->
            var currentLine = reader.readLine()
            while (currentLine != null) {
                val buffer = currentLine
                currentLine = reader.readLine()

                try {
                    val csvEntries = buffer.split(",")
                    if (csvEntries.size != 2) continue
                    val tischler = Tischler.fromString(csvEntries[0]) ?: continue
                    val date = LocalDateTime.parse(csvEntries[1])
                    val entry = entries[tischler]
                    when (entry) {
                        null -> entries.put(tischler, mutableListOf(date))
                        else -> entries.put(tischler, (entry + date).toMutableList())
                    }

                } catch (e: Exception) {
                    Timber.w("Error reading Entry from file. $e")
                }
            }
        }

        return entries.toSortedMap()
    }

    /**
     * Makes sure cache dir does not get too big.
     * The maximum size is defined by 2 things, and always picks the lower boundary
     * * [MAX_LOG_DIR_SIZE] For API < 26
     * * [StorageManager.getCacheQuotaBytes] For API >= 26
     *
     * The log directory only allows files with UNIX-timestamps as names. Every other file
     * and possible subfolder will be deleted first, BEFORE deleting the oldest log files
     */
    private fun pruneLogDir() {
        // Make sure cache dir does not get too big
        if (!dataDir.exists() && !dataDir.mkdirs()) {
            return
        }

        val files = dataDir.listFiles() ?: return

        // Remove all files that are not valid log files == filename has to be a unix timestamp
        files
            .filterNot { it.name.isDigitsOnly() }
            .forEach { file ->
                val size = file.size
                Timber.w("Deleting not recognized log ${if (file.isDirectory) "directory" else "file"} \"${file.name}\" of size: $size")
                file.deleteRecursively()
            }

        var folderSize = dataDir.size
        if (folderSize > cacheSpaceQuota || folderSize > MAX_LOG_DIR_SIZE) {
            Timber.w("Log directory too big")
        }

        files.forEach { file ->
            if (folderSize <= cacheSpaceQuota && folderSize <= MAX_LOG_DIR_SIZE) {
                return
            }

            val size = file.size
            folderSize -= size
            Timber.w("Deleting cached log ${if (file.isDirectory) "directory" else "file"} \"${file.name}\" of size: $size")
            file.deleteRecursively()
        }
    }

}