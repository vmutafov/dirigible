/*
 * Generated by Eclipse Dirigible based on model and template.
 *
 * Do not modify the content as it may be re-generated again.
 */
exports.getTemplate = function () {
    return {
        name: "OpenUI5 - List",
        description: "OpenUI5 Template - List",
        sources: [
            {
                location: "/template-openui5-list/ui/Component.js",
                rename: "/Component.js",
                action: "copy"
            },
            {
                location: "/template-openui5-list/ui/index.html",
                rename: "/index.html",
                action: "copy"
            },
            {
                location: "/template-openui5-list/ui/manifest.json",
                rename: "/manifest.json",
                action: "copy"
            },
            {
                location: "/template-openui5-list/ui/controller/List.controller.js",
                rename: "/controller/List.controller.js",
                action: "copy"
            },
            {
                location: "/template-openui5-list/ui/mockdata/products.json",
                rename: "/mockdata/products.json",
                action: "copy"
            },
            {
                location: "/template-openui5-list/ui/view/List.view.xml",
                rename: "/view/List.view.xml",
                action: "copy"
            }
        ],
        parameters: []
    };
};