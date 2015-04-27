# General #
- Discuss the project's license. I chose LGPL at the beginning, but maybe we could choose a less restrictive license (e.g., MIT)?

# Maven #
- Add the license & header to the files (http://code.google.com/p/maven-license-plugin/)
- Fill-in the SCM info in the pom.xml
- Fill-in the issue management info in the pom.xml
- Once a stable version is available, put on the official maven repository
- Add versions for all plugins (report & build)
- Update dependencies to more recent versions
- Create the maven site (src/site/...)

# Code related #
- Clean up / Refactor / Design
- Use java.util.logging or SLF4J instead of Log4j for the logging
- Remove JDOM dependency?
- Add proxy support
- Internationalize API messages/exceptions
- Setup a test Google Reader account
- Write unit tests and 'integration tests'
- Provide better documentation
- Add samples

# Features to implement #
- A method to mark an item as read
- A method to mark a feed as read

- Quick add subscription:
https://www.google.com/reader/api/0/subscription/quickadd
POST /reader/api/0/subscription/quickadd
quickadd=

&lt;TERM&gt;


T=

&lt;TOKEN&gt;



- Feed search
> https://www.google.com/reader/directory/search?q=

&lt;TERM&gt;


> GET /reader/directory/search?q=

&lt;TERM&gt;



- Edit subscriptions:
subscription/edit
> s 	feed 	The subscription feed name, in the form feed/...
> t 	title 	The subscription title, used when adding a new subscription or when changing a subscription name
> a 	add 	A label to add (a label on a subscription is called a folder) in the form user/...
> r 	remove 	A label to remove (a label on a subscription is called a folder) in the form user/...
> ac 	action 	The actions to do. Know values are edit (to add/remove label/forlder to a feed), 'subscribe', 'unsubscribe'
> token 	token 	The mandatory up to date token

- Edit tags:
> tag/edit
> > s 	feed 	The tag/folder name seen as a feed
> > pub 	public 	A boolean string true or false. When true, the tag/folder will become public. When false, the tag/folder will stop being public.
> > token 	token 	The mandatory up to date token


> edit-tag
> > i 	entry 	The item/entry to edit, in the form tag:google.com,2005:reader/item/... ( it's the xml id of the entry tag of the atom feed)
> > a 	add 	A label/state to add (a label on an item/entry is called a tag) in the form user/...
> > r 	remove 	A label/state to remove (a label on an item/entry is called a tag) in the form user/...
> > ac 	action 	The actions to do. Know value is edit (to add/remove label/forlder to a feed)
> > token 	token 	The mandatory up to date token

- Disable tags:

> disable-tag
> > s 	feed 	The tag/folder name seen as a feed
> > ac 	action 	The actions to do. Know value is disable-tags (to remove a tag/folder)
> > token 	token 	The mandatory up to date token

- Add a tag to a feed, an item or both (POST)

> /reader/api/0/edit-tag -- base URL
> s=feed%2F[URL](feed.md) -- the feed URL you would like to tag
> i=[id](item.md) -- the item ID presented in the feed. Optional and used to tag individual items.
> a=user%2F[ID](user.md)%2Flabel%2F[tag](tag.md) -- requested action. add a tag to the feed, item, or both.
> T=

&lt;TOKEN&gt;



- Change READ Status (read/unread)
> POST /reader/api/0/edit-tag
> s=feed%2Fhttp%3A%2F%2Fwww.linuxjournal.com%2Fnews.rss
> i=tag%3Agoogle.com%2C2005%3Areader%2Fitem%2Fcf9fb763b3c10080
> ac=edit-tags
> a=user%2F15487536670345822901%2Fstate%2Fcom.google%2Fread
> async=true
> T=

&lt;TOKEN&gt;



- Change STARRED Status (star/unstar)
> POST /reader/api/0/edit-tag
> s=feed%2Fhttp%3A%2F%2Ffeeds.downloadsquad.com%2Fweblogsinc%2Fdownloadsquad
> i=tag%3Agoogle.com%2C2005%3Areader%2Fitem%2Fdea27b86012de047
> ac=edit-tags
> a=user%2F15487536670345822901%2Fstate%2Fcom.google%2Fstarred
> async=true
> T=

&lt;TOKEN&gt;



- Change SHARED Status (share/unshare)
> POST /reader/api/0/edit-tag
> s=feed%2Fhttp%3A%2F%2Ffeeds.downloadsquad.com%2Fweblogsinc%2Fdownloadsquad
> i=tag%3Agoogle.com%2C2005%3Areader%2Fitem%2Fdea27b86012de047
> ac=edit-tags
> a=user%2F15487536670345822901%2Fstate%2Fcom.google%2Fbroadcast
> async=true
> T=

&lt;TOKEN&gt;



- For requests returning ATOM feeds:
> n 	 count 	 Number of items returns in a set of items (default 20)
> client 	client 	The default client name (see client in glossary)
> r 	order 	By default, items starts now, and go back time. You can change that by specifying this key to the value o (default value is d)
> ot 	start\_time 	The time (unix time, number of seconds from January 1st, 1970 00:00 UTC) from which to start to get items. Only works for order r=o mode. If the time is older than one month ago, one month ago will be used instead.
> xt 	exclude\_target 	another set of items suffix, to be excluded from the query. For exemple, you can query all items from a feed that are not flagged as read. This value start with feed/ or user/, not with !http:// or www
> c 	continuation 	a string used for continuation process. Each feed return not all items, but only a certain number of items. You'll find in the atom feed (under the name gr:continuation) a string called continuation. Just add that string as argument for this parameter, and you'll retrieve next items.

- Items shared by friends:
> https://www.google.com/reader/view/user/-/state/com.google/broadcast-friends


Other features that could be added:
- Items that were emailed
> http://www.google.com/reader/view/user/-/state/com.google/tracking-emailed

- All the posts you have marked as unread at some point
> http://www.google.com/reader/view/user/-/state/com.google/tracking-kept-unread

- All the posts you have marked as unread and are still unread (Google Reader calls them "saved items"):
> http://www.google.com/reader/view/user/-/state/com.google/kept-unread

- Did you click on a post's main link to go to the original location? You'll find it here:
> http://www.google.com/reader/view/user/-/state/com.google/tracking-item-link-used

- Did you click on a link from a post's content? The post should be included in this list:
> http://www.google.com/reader/view/user/-/state/com.google/tracking-body-link-used

- Are Google's recommended feeds interesting? Check the list of recommended feeds you've subscribed to:
> http://www.google.com/reader/view/user/-/state/com.google/recommendations-subscribed

- Did you accidentally dismiss a recommended feed? You'll find it here:
> http://www.google.com/reader/view/user/-/state/com.google/recommendations-dismissed

- Share with note?
- Extract information from Google Reader Trends?

- Preferences:
> https://www.google.com/reader/api/0/preference/stream/list?output=json or xml