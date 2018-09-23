1.写个方法   searchText()

	private void searchText() {
        String text = etInput.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {

            if(items != null && items.size() >0){
                items.clear();
            }

            try {
                text = URLEncoder.encode(text, "UTF-8");

                url = Constants.SEARCH_URL + text;
                getDataFromNet();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void getDataFromNet() {
        progressBar.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams(url);
       x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void processData(String result) {
        SearchBean searchBean = parsedJson(result);
        items =  searchBean.getItems();

        showData();
    }

    private void showData() {
        if(items != null && items.size() >0){
            //设置适配器
            adapter = new SearchAdapter(this,items);
            listview.setAdapter(adapter);
            tvNodata.setVisibility(View.GONE);
        }else{
            tvNodata.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }


        progressBar.setVisibility(View.GONE);
    }

    /**
     * 解析json数据
     * @param result
     * @return
     */
    private SearchBean parsedJson(String result) {
        return new Gson().fromJson(result, SearchBean.class);
    }


2.得到bean对象   SearchBean


3.得到地址    Constants

4.适配器  SearchAdapter