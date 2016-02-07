# Key Translation

Service to translate foreign ids to my own ids

## Contents

  * Multi-tenant structure
  * In-memory (``atom``) persistence
  
## Deployment

Can be used in an embedded server or deployed to an application server

* Standalone server 
  * compile and package: ``lein ring uberwar``
  * then deploy
  * has been tested in a TomEE JaxRS 1.7.3 ([download][download-link], [release-notes][release-notes]), under Windows 7 64bits
 
 

* Embedded server
  * ``lein ring server``

[download-link]: http://tomee.apache.org/downloads.html
[release-notes]: http://tomee.apache.org/tomee-1.7.3-release-notes.html

### Notes

This project has been manually forked from [rest-spike-liberator-clojure](https://github.com/alvarogarcia7/rest-spike-liberator-clojure) as of commit [3aaab78](https://github.com/alvarogarcia7/rest-spike-liberator-clojure/commit/3aaab78f37e821a6e94f43c197f4f3470fe2eaaf)
