#import "app_delegate.h"
#import <Foundation/Foundation.h>
#import <GoogleMobileAds/GADRewardedAdDelegate.h>
#import <GoogleMobileAds/GADRewardedAd.h>

@interface AdmobRewarded : NSObject <GADRewardedAdDelegate>
{
    ViewController *rootController;
    bool isReal;
    int instanceId;
}
@property(nonatomic, strong) NSMutableDictionary<NSString *, GADRewardedAd *> *rewardedVideoAds;

- (void)initialize:(BOOL)isReal
                  :(int)instanceID;
- (void)loadRewardedVideo:(NSString *)rewardedId;
- (void)showRewardedVideo:(NSString *)rewardedId;

@end