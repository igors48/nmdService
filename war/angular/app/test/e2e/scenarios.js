'use strict';

describe('my app', function() {

  browser.get('http:/localhost:8080');

  it('should automatically redirect to /feeds when location hash/fragment is empty', function() {
    expect(browser.getLocationAbsUrl()).toMatch("/feeds");
    
    element(by.model('feedLink')).sendKeys('Julie');
    element(by.id('add-button')).click();
  });

});