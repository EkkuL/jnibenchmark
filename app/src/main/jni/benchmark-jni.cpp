#include <jni.h>

#include <vector>
#include <cmath>
#include <cstring>
#include <iostream>

#ifdef __cplusplus
extern "C" {
#endif

static long IM = 139968;
static long IA = 3877;
static long IC = 29573;

inline double getRandom(double max) {
    static long last = 42;
    last = (last * IA + IC) % IM;
    return( max * last / IM );
}


std::vector<int> runSieve(int upperBound){

    std::vector<int>primes;

    int upperBoundSquareRoot = (int)sqrt((double)upperBound);
    bool *isComposite = new bool[upperBound + 1];
    memset(isComposite, 0, sizeof(bool) * (upperBound + 1));
    for (int m = 2; m <= upperBoundSquareRoot; m++) {
        if (!isComposite[m]) {
            primes.push_back(m);
            for (int k = m * m; k <= upperBound; k += m)
                isComposite[k] = true;
        }
    }
    for (int m = upperBoundSquareRoot; m <= upperBound; m++)
        if (!isComposite[m])
            primes.push_back(m);
    delete [] isComposite;

    return primes;
}

#ifdef __cplusplus
}
#endif


//JNI Methods
#ifdef __cplusplus
extern "C" {
#endif
JNIEXPORT jintArray JNICALL
        Java_com_coffeeintocode_jnibenchmark_MainActivity_runSieve(JNIEnv *env,
                                                                      jobject instance, jint upperBound);

JNIEXPORT jdouble JNICALL
Java_com_coffeeintocode_jnibenchmark_MainActivity_getRandom(JNIEnv *env, jobject instance,
        jdouble max);

#ifdef __cplusplus
}
#endif

JNIEXPORT jintArray JNICALL
Java_com_coffeeintocode_jnibenchmark_MainActivity_runSieve(JNIEnv *env,
                                                                      jobject instance, jint upperBound){
    //Call the native method
    std::vector <int> vec = runSieve(upperBound);
    //Wrapper to turn the vector into jintArray
    jintArray primes = (*env).NewIntArray(vec.size());
    //Copy the contents
    (*env).SetIntArrayRegion(primes, 0, vec.size(), (jint * ) &vec[0]);

    return primes;
}

JNIEXPORT jdouble JNICALL
Java_com_coffeeintocode_jnibenchmark_MainActivity_getRandom(JNIEnv *env, jobject instance,
                                                            jdouble max) {

    return getRandom(max);

}