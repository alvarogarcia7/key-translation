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

## Tests

### Automated 

``lein test``

### Manual

 * Start the server (see Deployment)
 * Set up the environment: ``export SRV="localhost:3000"``
 * Load data with POST requests
 * Query data with GET requests

Sample POST with cURL (see tests):

```bash
curl 
    --form "event=@dev-resources/vc.csv" \
    --form "id=@dev-resources/vc.csv" \
    -X POST $SRV/tenants/1003/events/23
```

Sample GET with cURL (see tests):

```bash
curl -X GET $SRV/tenants/1003/events/010100120001
# 2649abbb5522855870d200cbbd5488a6
```

## Notes

This project has been manually forked from [rest-spike-liberator-clojure](https://github.com/alvarogarcia7/rest-spike-liberator-clojure) as of commit [3aaab78](https://github.com/alvarogarcia7/rest-spike-liberator-clojure/commit/3aaab78f37e821a6e94f43c197f4f3470fe2eaaf)
