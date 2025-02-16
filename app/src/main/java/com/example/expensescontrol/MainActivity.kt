package com.example.expensescontrol

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

// Constants
// The authority for the sync adapter's content provider
const val AUTHORITY = "com.example.expensescontrol.syncadapter.provider"
// An account type, in the form of a domain name
const val ACCOUNT_TYPE = "example.com"
// The account name
const val ACCOUNT = "placeholderaccount"

class MainActivity : ComponentActivity() {
    // Instance fields
    private lateinit var mAccount: Account
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create the placeholder account
        mAccount = createSyncAccount()
        enableEdgeToEdge()
        setContent {
            ExpensesApp()
            }
        }
    /**
     * Create a new placeholder account for the sync adapter
     */
    private fun createSyncAccount(): Account {
        val accountManager = getSystemService(ACCOUNT_SERVICE) as AccountManager
        return Account(ACCOUNT, ACCOUNT_TYPE).also { newAccount ->
            /*
             * Add the account and account type, no password or user data
             * If successful, return the Account object, otherwise report an error.
             */
            if (accountManager.addAccountExplicitly(newAccount, null, null)) {
                /*
                 * If you don't set android:syncable="true" in
                 * in your <provider> element in the manifest,
                 * then call context.setIsSyncable(account, AUTHORITY, 1)
                 * here.
                 */
            } else {
                /*
                 * The account exists or some other error occurred. Log this, report it,
                 * or handle it internally.
                 */
            }
        }
        }
    }