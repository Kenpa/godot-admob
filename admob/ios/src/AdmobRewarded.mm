#import "AdmobRewarded.h"
#import <GoogleMobileAds/GADRewardedAd.h>
#import <GoogleMobileAds/GADAdReward.h>
#import <GoogleMobileAds/GADRequest.h>
#import <GoogleMobileAds/GADRequestError.h>
#include "reference.h"


@implementation AdmobRewarded

- (void)initialize:(BOOL)_isReal :(int)_instanceId {
    isReal = _isReal;
    instanceId = _instanceId;
    rootController = [AppDelegate getViewController];

    self.rewardedVideoAds = [NSMutableDictionary<NSString *, GADRewardedAd *> dictionary];
}

- (GADRewardedAd *)createAndLoadRewardedAd:(NSString*) id {
    Object *obj = ObjectDB::get_instance(instanceId);
    GADRewardedAd *rewardedAd = [[GADRewardedAd alloc] initWithAdUnitID:id];
    GADRequest *request = [GADRequest request];
    [rewardedAd loadRequest:request completionHandler:^(GADRequestError * _Nullable error) {
        if (error) {
            NSLog(@"Admob: onRewardedAdFailedToLoad: %@", id);
            obj->call_deferred("_on_rewarded_ad_failed_to_load", [id UTF8String], (int)error.code);
        } else {
            NSLog(@"Admob: onRewardedAdLoaded: %@", id);
            obj->call_deferred("_on_rewarded_ad_loaded", [id UTF8String]);
        }
    }];
    return rewardedAd;
}

- (void)loadRewardedVideo:(NSString*) id {
    [self.rewardedVideoAds removeObjectForKey: id];
    [self.rewardedVideoAds setObject: [self createAndLoadRewardedAd: id] forKey: id];
}

- (void)showRewardedVideo:(NSString*) id {
    if (self.rewardedVideoAds[id] != nil) {
        if (self.rewardedVideoAds[id].isReady) {
            [self.rewardedVideoAds[id] presentFromRootViewController:rootController delegate:self];
        }
    } else {
        NSLog(@"RewardedAd was not loaded yet.");
    }
} 

- (void)rewardedAdDidDismiss:(GADRewardedAd *)rewardedAd {
    [self loadRewardedVideo: rewardedAd.adUnitID];
    Object *obj = ObjectDB::get_instance(instanceId);
    NSLog(@"Admob: onRewardedAdClosed: %@", rewardedAd.adUnitID);
    obj->call_deferred("_on_rewarded_ad_closed", [rewardedAd.adUnitID UTF8String]);
}

- (void)rewardedAd:(GADRewardedAd *)rewardedAd userDidEarnReward:(GADAdReward *)reward {
    Object *obj = ObjectDB::get_instance(instanceId);
    NSLog(@"Admob: onRewarded: %@", rewardedAd.adUnitID);
    obj->call_deferred("_on_rewarded", [rewardedAd.adUnitID UTF8String]);
}

- (void)rewardedAdDidPresent:(GADRewardedAd *)rewardedAd {
    Object *obj = ObjectDB::get_instance(instanceId);
    NSLog(@"Admob: onRewardedAdOpened: %@", rewardedAd.adUnitID);
    obj->call_deferred("_on_rewarded_ad_opened", [rewardedAd.adUnitID UTF8String]);
}

- (void)rewardedAd:(GADRewardedAd *)rewardedAd didFailToPresentWithError:(NSError *)error {
    Object *obj = ObjectDB::get_instance(instanceId);
    NSLog(@"Admob: onRewardedAdFailedToShow: %@", rewardedAd.adUnitID);
    obj->call_deferred("_on_rewarded_ad_failed_to_show", [rewardedAd.adUnitID UTF8String], (int)error.code);
}

@end
