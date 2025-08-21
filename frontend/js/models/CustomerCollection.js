define(['ojs/ojmodel', 'models/CustomerModel'], function (oj, CustomerModel) {
    var CustomerCollection = oj.Collection.extend({
        url: 'http://localhost:9090/customers',
        model: CustomerModel
    });
    return CustomerCollection;
});
