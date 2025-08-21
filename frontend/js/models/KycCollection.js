define(['ojs/ojmodel', 'models/KycModel'], function (oj, KycModel) {
    var KycCollection = oj.Collection.extend({
        url: 'http://localhost:9090/customers/kyc',
        model: KycModel
    });
    return KycCollection;
});
