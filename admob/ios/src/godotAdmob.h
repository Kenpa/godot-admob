#ifndef GODOT_ADMOB_H
#define GODOT_ADMOB_H

#include <version_generated.gen.h>

#include "reference.h"

#ifdef __OBJC__
@class AdmobRewarded;
typedef AdmobRewarded *rewardedPtr;
#else
typedef void *rewardedPtr;
#endif

class GodotAdmob : public Reference
{

    GDCLASS(GodotAdmob, Reference);

    bool isReal;
    int instanceId;

    rewardedPtr rewarded;

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
