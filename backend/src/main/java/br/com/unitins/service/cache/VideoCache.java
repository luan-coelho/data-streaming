package br.com.unitins.service.cache;

import br.com.unitins.domain.Video;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;

@ApplicationScoped
public class VideoCache {

    private final int MAXIMUM_CACHED_VIDEOS = 10;

    private final Cache<String, Object> cache = Caffeine.newBuilder().maximumSize(MAXIMUM_CACHED_VIDEOS).build();

    public void put(String key, Object video) {
        if (getNumberOfVideos() == MAXIMUM_CACHED_VIDEOS) {
//            evictLeastAccessedVideo();
        }
        cache.put(key, video);
    }

    public Object get(String key) {
        return cache.getIfPresent(key);
    }

    public void invalidate(String key) {
        cache.invalidate(key);
    }

    public int getNumberOfVideos() {
        return (int) cache.estimatedSize();
    }

//    public void evictLeastAccessedVideo() {
//        String leastAccessedKey = null;
//        long leastAccessedValue = Long.MAX_VALUE;
//
//        for (Map.Entry<String, Video> entry : cache.asMap().entrySet()) {
//            long accessCount = entry.getValue().getViews();
//            if (accessCount < leastAccessedValue) {
//                leastAccessedKey = entry.getKey();
//                leastAccessedValue = accessCount;
//            }
//        }
//
//        if (leastAccessedKey != null) {
//            cache.invalidate(leastAccessedKey);
//        }
//    }
}
