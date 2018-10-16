# 配置路线

## site复用

site代表着一个域名的整体配置,包含

|字段名|含义|补充|
|-----|----|---|
|domain|域名|一个被爬取网站的统一标识,会被用于cookie域以及可以通过domain查询整体配置信息|
|userAgent|访问的浏览器标识|默认chrome最新版本,也可选择固定数组自动切换|
|defaultCookies|默认cookie|每次访问会自动携带|
|extras|额外头信息|键值对,默认会被携带到head中|
|charset|编码|网站内容编码,默认utf-8|
|sleeptime|间隔时间|每次爬取间隔时间,单位ms,默认100ms|
|retryTimes|失败重试次数|当一个请求失败时,会重新尝试的次数|
|retrySleepTimes|重试间隔时间|一个请求失败时,会重试的间隔时间|
|timeOut|超时时间|超时时间|
|acceptStatCode|默认成功的code标识|
