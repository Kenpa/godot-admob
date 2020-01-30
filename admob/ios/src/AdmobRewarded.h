#import "app_delegate.h"
#import <GoogleMobileAds/GADRewardedAdDelegate.h>

@interface AdmobRewarded : NSObject <GADRewardBasedVideoAdDelegate>
{
    ViewController *rootController;
}

- (void)initialize:(BOOL)is_real
                  :(int)instance_id;
- (void)loadRewardedVideo:(NSString *)rewardedId;
- (void)showRewardedVideo;

@end
