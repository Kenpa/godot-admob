package org.godotengine.godot;

import com.google.android.gms.ads.*;

import java.util;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

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

		MobileAds.initialize(this, new OnInitializationCompleteListener() {
			@Override
			public void OnInitializationComplete(InitializationStatus initilizationStatus) {
			}
		});
		Log.W(LOGTAG, "Initialized.");
	}

	public RewardedAd createAndLoadRewardedAd(final String id) {
		String actualId = this.isReal ? id : "ca-app-pub-3940256099942544/5224354917";
		RewardedAd rewardedAd = new RewardedAd(this, actualId);
		RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
			@Override
			public void onRewardedAdLoaded() {
				Log.w(LOGTAG, "onRewardedAdLoaded: " + actualId);
				GodotLib.calldeferred(instance_id, "_on_rewarded_ad_loaded", new Object[] { id });
			}

			@Override
			public void onRewardedAdFailedToLoad(int errorCode) {
				Log.w(LOGTAG, "onRewardedAdFailedToLoad: " + actualId + ". errorCode: " + errorCode);
				GodotLib.calldeferred(instance_id, "_on_rewarded_ad_failed_to_load", new Object[] { id, errorCode });
			}
		};
		rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
		return rewardedAd;
	}

	public void loadRewardedVideo(final String id) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (rewardedVideoAds == null) {
					rewardedVideoAds = new HashMap<>();
				}
				rewardedVideoAds.put(id, createAndLoadRewardedAd(id));
			}
		});
	}

	public void showRewardedVideo(final String id) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (rewardedVideoAds.get(id) != null && rewardedVideoAds.get(id).isLoaded()) {
					String actualId = this.isReal ? id : "ca-app-pub-3940256099942544/5224354917";
					RewardedAdCallback adCallback = new RewardedAdCallback() {
						@Override
						public void onRewardedAdOpened() {
							Log.w(LOGTAG, "onRewardedAdOpened: " + actualId);
							GodotLib.calldeferred(instance_id, "_on_rewarded_ad_opened", new Object[] { id });
						}

						@Override
						public void onRewardedAdClosed() {
							rewardedVideoAds.put(id, createAndLoadRewardedAd(id));
							Log.w(LOGTAG, "onRewardedAdClosed: " + actualId);
							GodotLib.calldeferred(instance_id, "_on_rewarded_ad_closed", new Object[] { id });
						}

						@Override
						public void onUserEarnedReward(@NonNull RewardItem reward) {
							Log.w(LOGTAG, "onRewarded: " + actualId);
							GodotLib.calldeferred(instance_id, "_on_rewarded", new Object[] { id });
						}

						@Override
						public void onRewardedAdFailedToShow(int errorCode) {
							Log.w(LOGTAG, "onRewardedAdFailedToShow: " + actualId);
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
