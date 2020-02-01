package org.godotengine.godot;

import java.util.Map;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class GodotAdMob extends Godot.SingletonBase {

	protected Activity activity = null;
	protected Context context;

	private int instance_id = 0;
	private boolean isReal = false;

	private Map<String, RewardedAd> rewardedVideoAds = null;

	public static final String LOGTAG = "GodotAdmob";

	public void init(boolean isReal, int instance_id) {
		this.isReal = isReal;
		this.instance_id = instance_id;
		this.rewardedVideoAds = new HashMap<>();

		MobileAds.initialize(this.activity, new OnInitializationCompleteListener() {
			@Override
			public void onInitializationComplete(InitializationStatus initilizationStatus) {
			}
		});
		Log.w(LOGTAG, "Initialized.");
	}

	public RewardedAd createAndLoadRewardedAd(final String id) {
		RewardedAd rewardedAd = new RewardedAd(this.activity, id);
		RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
			@Override
			public void onRewardedAdLoaded() {
				Log.w(LOGTAG, "onRewardedAdLoaded: " + id);
				GodotLib.calldeferred(instance_id, "_on_rewarded_ad_loaded", new Object[] { id });
			}

			@Override
			public void onRewardedAdFailedToLoad(int errorCode) {
				Log.w(LOGTAG, "onRewardedAdFailedToLoad: " + id + ". errorCode: " + errorCode);
				GodotLib.calldeferred(instance_id, "_on_rewarded_ad_failed_to_load", new Object[] { id, errorCode });
			}
		};
		AdRequest.Builder adReq = new AdRequest.Builder();
		if (!this.isReal) {
			adReq.addTestDevice("A951437A6DDDB2A43DF0EE9A04693747");
			Log.w(LOGTAG, "Test ads request");
		}
		rewardedAd.loadAd(adReq.build(), adLoadCallback);
		return rewardedAd;
	}

	public void loadRewardedVideo(final String id) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				rewardedVideoAds.put(id, createAndLoadRewardedAd(id));
			}
		});
	}

	public void showRewardedVideo(final String id) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (rewardedVideoAds.get(id) != null && rewardedVideoAds.get(id).isLoaded()) {
					RewardedAdCallback adCallback = new RewardedAdCallback() {
						@Override
						public void onRewardedAdOpened() {
							Log.w(LOGTAG, "onRewardedAdOpened: " + id);
							GodotLib.calldeferred(instance_id, "_on_rewarded_ad_opened", new Object[] { id });
						}

						@Override
						public void onRewardedAdClosed() {
							rewardedVideoAds.put(id, createAndLoadRewardedAd(id));
							Log.w(LOGTAG, "onRewardedAdClosed: " + id);
							GodotLib.calldeferred(instance_id, "_on_rewarded_ad_closed", new Object[] { id });
						}

						@Override
						public void onUserEarnedReward(@NonNull RewardItem reward) {
							Log.w(LOGTAG, "onRewarded: " + id);
							GodotLib.calldeferred(instance_id, "_on_rewarded", new Object[] { id });
						}

						@Override
						public void onRewardedAdFailedToShow(int errorCode) {
							Log.w(LOGTAG, "onRewardedAdFailedToShow: " + id);
							GodotLib.calldeferred(instance_id, "_on_rewarded_ad_failed_to_show", new Object[] { id, errorCode });
						}
					};
				} else {
					Log.w(LOGTAG, "RewardedAd was not loaded yet.");
				}
			}
		});
	}

	static public Godot.SingletonBase initialize(Activity activity) {
		return new GodotAdMob(activity);
	}

	public GodotAdMob(Activity p_activity) {
		registerClass("AdMob", new String[] { "init", "loadRewardedVideo", "showRewardedVideo" });
		this.activity = p_activity;
		this.context = activity.getApplicationContext();
	}
}
