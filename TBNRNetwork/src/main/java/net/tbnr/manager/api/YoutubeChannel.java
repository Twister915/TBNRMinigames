/*
 * Copyright (c) 2014.
 * Cogz Development LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

package net.tbnr.manager.api;

import lombok.Getter;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * <p>
 * Latest Change:
 * <p>
 *
 * @author Jake
 * @since 4/13/2014
 */
public class YoutubeChannel {
    @Getter private String username;
    @Getter private String location;
    @Getter private Integer viewCount;
    @Getter private Integer subscriberCount;
    @Getter private String channelURL;
    protected YoutuberCallback callback;

    public YoutubeChannel(String username, YoutuberCallback callback) {
        this.username = username;
        this.callback = callback;
        YoutuberRetriever youtuberRetriever = new YoutuberRetriever(username, callback, this);
        new Thread(youtuberRetriever).start();
    }

    public static class YoutuberRetriever implements Runnable {
        private String username;
        private YoutuberCallback callback;
        private URL channelURL;
        private YoutubeChannel channel;
        private JSONObject json;

        public YoutuberRetriever(String username, YoutuberCallback callback, YoutubeChannel channel) {
            this.username = username;
            this.callback = callback;
            this.channel = channel;
        }

        @Override
        public void run() {
            try {
                channelURL = new URL("http://gdata.youtube.com/feeds/api/users/" + username + "?v=2&alt=json");
                init();
                channel.subscriberCount = getSubscriberCount();
                channel.location = getLocation();
                channel.subscriberCount = getSubscriberCount();
                channel.viewCount = getTotalViews();
                channel.channelURL = "https://www.youtube.com/user/" + username;
            } catch (JSONException | ParseException | IOException e) {
                callback.onFail(channel, e.getMessage());
            }
            callback.onComplete(channel);
        }

        public void init() throws IOException, ParseException, JSONException {
            BufferedReader br = new BufferedReader(new InputStreamReader(channelURL.openStream()));
            String rawJSON = br.readLine();
            br.close();
            json = (JSONObject) new JSONParser().parse(rawJSON);
            json = (JSONObject) json.get("entry");
        }

        public String getLocation() throws JSONException {
            JSONObject json2 = (JSONObject) json.get("yt$location");
            return (String) json2.get("$t");
        }

        public int getSubscriberCount() throws JSONException {
            JSONObject json2 = (JSONObject) json.get("yt$statistics");
            String s = (String) json2.get("subscriberCount");
            return Integer.parseInt(s);
        }

        public int getTotalViews() throws JSONException {
            JSONObject json2 = (JSONObject) json.get("yt$statistics");
            String s = (String) json2.get("totalUploadViews");
            return Integer.parseInt(s);
        }
    }

    public static interface YoutuberCallback {
        public void onComplete(YoutubeChannel youtubeChannel);

        public void onFail(YoutubeChannel youtubeChannel, String errorMessage);
    }

    public static class YoutubeException extends Exception {
        public YoutubeException(String message) {
            super(message);
        }
    }
}
