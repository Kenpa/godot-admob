def can_build(plat):
    return plat == "android" or plat == "iphone"


def configure(env):
    if (env['platform'] == 'android'):
        env.android_add_dependency(
            "implementation ('com.google.android.gms:play-services-ads:18.3.0') { exclude group: 'com.android.support' }")
        env.android_add_java_dir("android")
        env.android_add_to_manifest("android/AndroidManifestChunk.xml")
        env.disable_module()

    if env['platform'] == "iphone":
        env.Append(FRAMEWORKPATH=['#modules/admob/ios/lib'])
        env.Append(CPPPATH=['#core'])
        env.Append(LINKFLAGS=['-ObjC'])
