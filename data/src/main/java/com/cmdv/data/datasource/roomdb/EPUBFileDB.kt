import com.cmdv.data.datasource.roomdb.EPUBFileDAO

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cmdv.data.entity.EPUBFileEntity

@Database(
    entities = [EPUBFileEntity::class],
    version = 3
)
abstract class EPUBFileDB : RoomDatabase() {

    abstract val epubFileDao: EPUBFileDAO

    companion object {
        @Volatile
        private var INSTANCE: EPUBFileDB? = null

        fun getInstance(context: Context): EPUBFileDB {
            synchronized(this) {
                var instance: EPUBFileDB? = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        EPUBFileDB::class.java,
                        "epub_file_database")
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }
    }
}