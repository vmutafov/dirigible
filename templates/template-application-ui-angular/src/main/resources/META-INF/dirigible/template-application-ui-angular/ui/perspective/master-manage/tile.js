/*
 * Generated by Eclipse Dirigible based on model and template.
 *
 * Do not modify the content as it may be re-generated again.
 */
exports.getTile = function () {
    return {
        group: "{{perspectiveName}}",
        name: "{{name}}",
        caption: "{{caption}}",
        tooltip: "{{tooltip}}",
        // icon: "file-o",
        location: "/services/v4/web/{{projectName}}/gen/ui/{{perspectiveName}}/index.html",
        order: "100"
    };
};
