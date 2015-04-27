'use strict';

describe('my app', function() {

  browser.get('http:/localhost:8080');

  it('should automatically redirect to /categories when location hash/fragment is empty', function() {
    expect(browser.getLocationAbsUrl()).toMatch("/categories");
    
    //element(by.model('feedLink')).sendKeys('Julie');
    //element(by.id('add-button')).click();
  });

});