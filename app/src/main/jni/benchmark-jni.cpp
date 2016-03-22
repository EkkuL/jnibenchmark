#include <jni.h>

#include <vector>
#include <cmath>
#include <cstring>
#include <iostream>
#include <map>
#include <sstream>
#include <unordered_map>

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

void heapsort(int n, double *ra) {
    int i, j;
    int ir = n;
    int l = (n >> 1) + 1;
    double rra;

    for (;;) {
        if (l > 1) {
            rra = ra[--l];
        } else {
            rra = ra[ir];
            ra[ir] = ra[1];
            if (--ir == 1) {
                ra[1] = rra;
                return;
            }
        }
        i = l;
        j = l << 1;
        while (j <= ir) {
            if (j < ir && ra[j] < ra[j+1]) { ++j; }
            if (rra < ra[j]) {
                ra[i] = ra[j];
                j += (i = j);
            } else {
                j = ir + 1;
            }
        }
        ra[i] = rra;
    }
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

int **mkmatrix(int rows, int cols) {
    int i, j, count = 1;
    int **m = (int **) malloc(rows * sizeof(int *));
    for (i=0; i<rows; i++) {
        m[i] = (int *) malloc(cols * sizeof(int));
        for (j=0; j<cols; j++) {
            m[i][j] = count++;
        }
    }
    return(m);
}

int **mmult(int rows, int cols, int **m1, int **m2, int **m3) {
    int i, j, k, val;
    for (i=0; i<rows; i++) {
        for (j=0; j<cols; j++) {
            val = 0;
            for (k=0; k<cols; k++) {
                val += m1[i][k] * m2[k][j];
            }
            m3[i][j] = val;
        }
    }
    return(m3);
}

const char* helloCat(int n){
    std::string str;
    size_t capacity = 31;
    str.reserve(capacity); // as per C-string
    size_t newLength = 6;
    for (int i = 0; i < n; i++)
    {
        if(newLength > capacity)
        {
            capacity *= 2;
            str.reserve(capacity);
        }
        str += "hello\n";
        newLength += 6;
    }
    return str.c_str();
}

int nestedloop(int n){
    int a, b, c, d, e, f, x=0;

    for (a=0; a<n; a++)
        for (b=0; b<n; b++)
            for (c=0; c<n; c++)
                for (d=0; d<n; d++)
                    for (e=0; e<n; e++)
                        for (f=0; f<n; f++)
                            x++;

    return(x);
}

void createHash(const char *values[], int size){
    std::unordered_map<int, std::string> hash;

    int n = 0;
    int keys[size];
    for( int i = 0 ; i < 3 ; i++ )
    {
        keys[i] = n;
        hash[ keys[i] ] = values[i];
        n += 10;
    }
}

long long fibo(long long a, long long b, int n) {
    return (--n>0)?(fibo(b, a+b, n)):(a);
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

JNIEXPORT jdoubleArray JNICALL
        Java_com_coffeeintocode_jnibenchmark_MainActivity_heapSort(JNIEnv *env, jobject instance,
                                                                   jint arraysize, jdoubleArray arr_);

JNIEXPORT void JNICALL
        Java_com_coffeeintocode_jnibenchmark_MainActivity_matrixMult(JNIEnv *env, jobject instance,
                                                                     jint size);
JNIEXPORT jstring JNICALL
        Java_com_coffeeintocode_jnibenchmark_MainActivity_helloCat(JNIEnv *env, jobject instance, jint n);

JNIEXPORT jint JNICALL
        Java_com_coffeeintocode_jnibenchmark_MainActivity_nestedloop(JNIEnv *env, jobject instance,
                                                                     jint n);
JNIEXPORT void JNICALL
        Java_com_coffeeintocode_jnibenchmark_MainActivity_createHash(JNIEnv *env, jobject instance, jint n,
                                                                     jobjectArray values);

JNIEXPORT jint JNICALL
        Java_com_coffeeintocode_jnibenchmark_MainActivity_fibonacci(JNIEnv *env, jobject instance,
                                                                    jint n);

#ifdef __cplusplus
}
#endif


//Implementations
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

JNIEXPORT jdoubleArray JNICALL
Java_com_coffeeintocode_jnibenchmark_MainActivity_heapSort(JNIEnv *env, jobject instance,
                                                           jint arraysize, jdoubleArray arr_) {
    jdouble *arr = env->GetDoubleArrayElements(arr_, NULL);

    heapsort(arraysize, arr);
    jdoubleArray  sorted = (*env).NewDoubleArray(arraysize);
    (*env).SetDoubleArrayRegion(sorted,0,arraysize,arr);

    env->ReleaseDoubleArrayElements(arr_, arr, 0);

    return sorted;
}

JNIEXPORT void JNICALL
Java_com_coffeeintocode_jnibenchmark_MainActivity_matrixMult(JNIEnv *env, jobject instance,
                                                             jint size) {

    int **m1 = mkmatrix(size, size);
    int **m2 = mkmatrix(size, size);
    int **mm = mkmatrix(size, size);

    mm = mmult(size, size, m1, m2, mm);


    return;
}

JNIEXPORT jstring JNICALL
Java_com_coffeeintocode_jnibenchmark_MainActivity_helloCat(JNIEnv *env, jobject instance, jint n) {

    return env->NewStringUTF(helloCat(n));
}

JNIEXPORT jint JNICALL
Java_com_coffeeintocode_jnibenchmark_MainActivity_nestedloop(JNIEnv *env, jobject instance,
                                                             jint n) {

    return(nestedloop(n));

}

JNIEXPORT void JNICALL
Java_com_coffeeintocode_jnibenchmark_MainActivity_createHash(JNIEnv *env, jobject instance, jint n,
                                                             jobjectArray values) {

    const char *strings[n];

    for(int i = 0; i < n; ++i){
        jstring string = (jstring) env->GetObjectArrayElement(values,i);
        const char *rawString = env->GetStringUTFChars(string, 0);
        strings[i] = rawString;
        env->DeleteLocalRef(string);
    }

    createHash(strings, n);


}


JNIEXPORT jint JNICALL
Java_com_coffeeintocode_jnibenchmark_MainActivity_fibonacci(JNIEnv *env, jobject instance,
                                                            jint n) {

    return((jint) fibo(0,1,n));
}