package sckdn.lisa.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(
        entities = {
                IllustHistoryEntity.class, //浏览历史
                IllustRecmdEntity.class, //推荐页保存历史数据
                DownloadEntity.class, //下载历史
                UserEntity.class, //用户保存信息
                SearchEntity.class, //搜索历史
                MuteEntity.class, //记录用户屏蔽的标签
                DownloadingEntity.class //记录用户正在下载中的列表
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "roomDemo-database";

    private static AppDatabase INSTANCE;

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }



    public abstract IllustRecmdDao recmdDao();

    public abstract DownloadDao downloadDao();

    public abstract SearchDao searchDao();

}

