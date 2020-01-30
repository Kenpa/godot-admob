#include "godotAdmob.h"
#import <GoogleMobileAds/GADMobileAds.h>
#import "app_delegate.h"

GodotAdmob::GodotAdmob() {
}

GodotAdmob::~GodotAdmob() {
}

void GodotAdmob::init(bool isReal, int instanceId) {
    isReal = is_real;
    instanceId = instance_id;

    rewarded = [AdmobRewarded alloc];
    [[GADMobileAds sharedInstance] startWithCompletionHandler:nil];
}

void GodotAdmob::loadRewardedVideo(const String &rewardedId) {
    NSString *idStr = [NSString stringWithCString:rewardedId.utf8().get_data() encoding: NSUTF8StringEncoding];
    [rewarded loadRewardedVideo: idStr];
}

void GodotAdmob::showRewardedVideo(const String &rewardedId) {
    NSString *idStr = [NSString stringWithCString:rewardedId.utf8().get_data() encoding: NSUTF8StringEncoding];
    [rewarded showRewardedVideo: idStr];
}

void GodotAdmob::_bind_methods() {
    ClassDB::bind_method("init",&GodotAdmob::init);
    ClassDB::bind_method("loadRewardedVideo",&GodotAdmob::loadRewardedVideo);
    ClassDB::bind_method("showRewardedVideo",&GodotAdmob::showRewardedVideo);
}
