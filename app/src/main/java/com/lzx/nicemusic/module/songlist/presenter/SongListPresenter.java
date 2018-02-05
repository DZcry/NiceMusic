package com.lzx.nicemusic.module.songlist.presenter;

import android.widget.Toast;

import com.lzx.musiclibrary.aidl.model.MusicInfo;
import com.lzx.nicemusic.R;
import com.lzx.nicemusic.base.mvp.factory.BasePresenter;
import com.lzx.nicemusic.helper.DataHelper;
import com.lzx.nicemusic.network.RetrofitHelper;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * @author lzx
 * @date 2018/2/5
 */

public class SongListPresenter extends BasePresenter<SongListContract.View> implements SongListContract.Presenter<SongListContract.View> {

    public int size = 10;
    private int offset = 0;
    private boolean isMore;

    @Override
    public void requestSongList(String title) {
        int type = getListType(title);
        RetrofitHelper.getMusicApi().requestMusicList(type, size, offset)
                .map(DataHelper::fetchJSONFromUrl)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    isMore = list.size() >= size;
                    mView.onGetSongListSuccess(list);
                }, throwable -> {

                });
    }

    @Override
    public void loadMoreSongList(String title) {
        if (isMore) {
            offset += 10;
            int type = getListType(title);
            RetrofitHelper.getMusicApi().requestMusicList(type, size, offset)
                    .map(DataHelper::fetchJSONFromUrl)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list -> {
                        isMore = list.size() >= size;
                        mView.loadMoreSongListSuccess(list);
                    }, throwable -> {
                        mView.loadFinishAllData();
                    });
        } else {
            mView.loadFinishAllData();
        }
    }


    private Integer[] songCoverArray = new Integer[]{
            R.drawable.image_song_list,
            R.drawable.image_korea,
            R.drawable.image_japan,
            R.drawable.image_mainland,
            R.drawable.image_occident,
            R.drawable.image_hongkong,
            R.drawable.image_europe,
            R.drawable.image_classic,
            R.drawable.image_love_song,
            R.drawable.image_television,
            R.drawable.image_internet
    };

    @Override
    public int getAlbumCover(String title) {
        return songCoverArray[getListTypeIndex(title)];
    }

    private int getListTypeIndex(String title) {
        int index = 0;
        switch (title) {
            case "我的歌单":
                index = 0;
                break;
            case "新歌榜":
                index = 1;
                break;
            case "热歌榜":
                index = 2;
                break;
            case "摇滚榜":
                index = 3;
                break;
            case "爵士":
                index = 4;
                break;
            case "流行":
                index = 5;
                break;
            case "欧美金曲榜":
                index = 6;
                break;
            case "经典老歌榜":
                index = 7;
                break;
            case "情歌对唱榜":
                index = 8;
                break;
            case "影视金曲榜":
                index = 9;
                break;
            case "网络歌曲榜":
                index = 10;
                break;
        }
        return index;
    }

    private int getListType(String title) {
        int type = 0;
        switch (title) {
            case "新歌榜":
                type = 1;
                break;
            case "热歌榜":
                type = 2;
                break;
            case "摇滚榜":
                type = 11;
                break;
            case "爵士":
                type = 12;
                break;
            case "流行":
                type = 16;
                break;
            case "欧美金曲榜":
                type = 21;
                break;
            case "经典老歌榜":
                type = 22;
                break;
            case "情歌对唱榜":
                type = 23;
                break;
            case "影视金曲榜":
                type = 24;
                break;
            case "网络歌曲榜":
                type = 25;
                break;
        }
        return type;
    }
}
