#ifndef GODOT_ADMOB_H
#define GODOT_ADMOB_H

#import <version_generated.gen.h>

#import "reference.h"

#ifdef __OBJC__
@class AdmobRewarded;
typedef AdmobRewarded *AdmobRewardedPtr;
#else
typedef void *AdmobRewardedPtr;
#endif

class GodotAdmob : public Reference
{

    GDCLASS(GodotAdmob, Reference);

    bool isReal;
    int instanceId;

    AdmobRewardedPtr rewarded;

protected:
    static void _bind_methods();

public:
    void init(bool isReal, int instanceId);
    void loadRewardedVideo(const String &rewardedId);
    void showRewardedVideo(const String &rewardedId);

    GodotAdmob();
    ~GodotAdmob();
};

#endif