#include <jni.h>

#ifndef _Included_org_on_puz_photobombsquad_MatAverager
#define _Included_org_on_puz_photobombsquad_MatAverager
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_org_on_puz_photobombsquad_MatAverager_average(
   JNIEnv * jenv, jclass, jlong matOut, jobjectArray* mats);

#ifdef __cplusplus
}
#endif
#endif
