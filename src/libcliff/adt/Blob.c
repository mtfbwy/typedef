interface Blob * Blob_malloc(const byte * a, const int n) {
    assert(n >= 0);
    void * p = mem_pick(sizeof(Blob) + n);
    MEMBER_SET(Blob, p, int, n, n);

    // ugly but enables the immutability of Blob
    int memberOffset = MEMBER_OFFSET(Blob, a);
    mem_copy((void *) ((word) p + memberOffset), a, n);
    return (Blob *) p;
}

interface void Blob_free(Blob * caller) {
    mem_drop(caller);
}

interface int Blob_compare(const Blob * caller, const Blob * another) {
    if (another == NULL) {
        return 1;
    }
    if (caller == another) {
        return 0;
    }
    int result = mem_compare(caller->a, another->a, (caller->n <= another->n ? caller->n : another->n));
    if (result != 0) {
        return result;
    }
    if (caller->n != another->n) {
        return caller->n < another->n ? -1 : 1;
    }
    return 0;
}
