define(['ojs/ojmodel'], function (oj) {
    var CustomerModel = oj.Model.extend({
        idAttribute: 'customerId',
        urlRoot: 'http://localhost:9090/customers'
    });
    return CustomerModel;
});
