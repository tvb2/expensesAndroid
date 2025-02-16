package com.example.expensescontrol.data

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal

/*
 * Define an implementation of ContentProvider that stubs out
 * all methods
 */
class StubProvider : ContentProvider() {
    /*
     * Always return true, indicating that the
     * provider loaded correctly.
     */
    override fun onCreate(): Boolean  = true
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        TODO("Not yet implemented")
    }

    /*
     * Return no type for MIME type
     */
    override fun getType(uri: Uri): String?  = null

    /*
     * query() always returns no results
     *
     */
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        queryArgs: Bundle?,
        cancellationSignal: CancellationSignal?
    ): Cursor? = null

    /*
     * insert() always returns null (no URI)
     */
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    /*
     * delete() always returns "no rows affected" (0)
     */
    override fun delete(uri: Uri, extras: Bundle?): Int = 0
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }

    /*
     * update() always returns "no rows affected" (0)
     */
    override fun update(uri: Uri, values: ContentValues?, extras: Bundle?): Int = 0
}