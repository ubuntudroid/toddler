package ubuntudroid.github.io.toddler.main.ui

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import ubuntudroid.github.io.toddler.R
import ubuntudroid.github.io.toddler.User
import ubuntudroid.github.io.toddler.events.AccountChangedEvent
import ubuntudroid.github.io.toddler.main.di.DaggerMainComponent
import ubuntudroid.github.io.toddler.provider.jira.JiraService
import ubuntudroid.github.io.toddler.provider.jira.models.IssueResponse
import javax.inject.Inject

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [TaskRecyclerViewAdapter.OnIssueInteractionListener]
 * interface.
 */
class TaskFragment : Fragment(), TaskRecyclerViewAdapter.OnIssueInteractionListener {
    @Inject
    lateinit var jiraService: JiraService

    @Inject
    lateinit var application: Application

    @Inject
    lateinit var appBus: Bus

    private var recyclerAdapter: TaskRecyclerViewAdapter? = null

    private var columnCount = 1
    private var menu: Menu? = null

    private var requestSubscription: Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            columnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_task_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.context
            if (columnCount <= 1) {
                view.layoutManager = LinearLayoutManager(context)
            } else {
                view.layoutManager = GridLayoutManager(context, columnCount)
            }
            recyclerAdapter = TaskRecyclerViewAdapter(this)
            view.adapter = recyclerAdapter
        }
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        DaggerMainComponent.create().inject(this)
        appBus.register(this)
        refresh()
    }

    override fun onDetach() {
        appBus.unregister(this)
        super.onDetach()
    }

    override fun onIssueClick(item: IssueResponse.Issue?) {
        Toast.makeText(context, "Issue ${item?.key} has been clicked", Toast.LENGTH_SHORT).show()
    }

    @Subscribe
    fun onAccountChanged(accountChangedEvent: AccountChangedEvent) {
        refresh()
    }

    private fun refresh() {
        requestSubscription?.unsubscribe()
        recyclerAdapter?.clear()

        if (context == null && !User.isUserAvailable(application)) {
            return
        }

        requestSubscription = jiraService
                .performJqlQuery("assignee=${User.getLogin(application)} AND status != Done AND status != \"Won't fix\" ORDER BY priority ASC", 0, -1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io()) // workaround for https://github.com/square/okhttp/issues/1592
                .subscribe(object : Subscriber<IssueResponse>() {
                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                    }

                    override fun onCompleted() {
                        Toast.makeText(application, "Query done", Toast.LENGTH_SHORT).show()
                    }

                    override fun onNext(response: IssueResponse?) {
                        recyclerAdapter?.appendIssues(response?.issues)
                    }
                })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        this.menu = menu
        inflater?.inflate(R.menu.menu_fragment_task, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        when (id) {
            R.id.action_refresh -> {
                refresh()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {

        private val ARG_COLUMN_COUNT = "column-count"

        @SuppressWarnings("unused")
        fun newInstance(columnCount: Int): TaskFragment {
            val fragment = TaskFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}
