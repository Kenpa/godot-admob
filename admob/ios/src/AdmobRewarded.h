#import "app_delegate.h"
#import <Foundation/Foundation.h>
#import <GoogleMobileAds/GADRewardedAdDelegate.h>

@interface AdmobRewarded : NSObject <GADRewardedAdDelegate>
{
    ViewController *rootController;
    bool isReal;
    int instanceId;
    NSMutableDictionary *rewardedVideoAds;
}

- (void)initialize:(BOOL)isReal:(int)instanceID;
- (void)loadRewardedVideo:(NSString *)rewardedId;
- (void)showRewardedVideo:(NSString *)rewardedId;

@end