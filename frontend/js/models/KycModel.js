define(['ojs/ojmodel'], function (oj) {
    var KycModel = oj.Model.extend({
        idAttribute: 'kycId',
        urlRoot: 'http://localhost:9090/customers/kyc'
    });
    return KycModel;
});
