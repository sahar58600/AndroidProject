{
  "fields": [
    {
      "name": "id",
      "type": "string"
    },
    {
      "default": null,
      "name": "post_id",
      "type": [
        "null",
        "string"
      ]
    },
    {
      "name": "update_time",
      "type": "long"
    },
    {
      "name": "channel",
      "type": {
        "fields": [
          {
            "name": "name",
            "type": "string"
          },
          {
            "default": null,
            "name": "id",
            "type": [
              "null",
              "string"
            ]
          }
        ],
        "name": "Channel",
        "type": "record"
      }
    },
    {
      "name": "original_content",
      "type": "string"
    },
    {
      "name": "summary",
      "type": "string"
    },
    {
      "default": null,
      "name": "media_url",
      "type": [
        "null",
        "string"
      ]
    },
    {
      "name": "source_language",
      "type": "string"
    },
    {
      "name": "title",
      "type": "string"
    },
    {
      "name": "type",
      "type": "string"
    },
    {
      "name": "city",
      "type": "string"
    },
    {
      "name": "place",
      "type": "string"
    },
    {
      "default": null,
      "name": "involves",
      "type": [
        "null",
        "string"
      ]
    },
    {
      "name": "casualties",
      "type": {
        "fields": [
          {
            "name": "blue_side",
            "type": {
              "fields": [
                {
                  "default": null,
                  "name": "lightly_injured",
                  "type": [
                    "null",
                    "string"
                  ]
                },
                {
                  "default": null,
                  "name": "moderately_injured",
                  "type": [
                    "null",
                    "string"
                  ]
                },
                {
                  "default": null,
                  "name": "seriously_injured",
                  "type": [
                    "null",
                    "string"
                  ]
                },
                {
                  "default": null,
                  "name": "critical_injured",
                  "type": [
                    "null",
                    "string"
                  ]
                },
                {
                  "default": null,
                  "name": "deaths",
                  "type": [
                    "null",
                    "string"
                  ]
                }
              ],
              "name": "Side",
              "type": "record"
            }
          },
          {
            "name": "red_side",
            "type": "Side"
          }
        ],
        "name": "Casualties",
        "type": "record"
      }
    },
    {
      "default": null,
      "name": "source",
      "type": [
        "null",
        "string"
      ]
    },
    {
      "default": null,
      "name": "date",
      "type": [
        "null",
        "string"
      ]
    },
    {
      "default": null,
      "name": "time",
      "type": [
        "null",
        "string"
      ]
    },
    {
      "name": "verification_status",
      "type": "string"
    },
    {
      "name": "incident_priority",
      "type": "string"
    },
    {
      "name": "tags",
      "type": "string"
    },
    {
      "name": "geo",
      "type": {
        "fields": [
          {
            "default": null,
            "name": "regions",
            "type": [
              "null",
              "string"
            ]
          },
          {
            "default": null,
            "name": "locations",
            "type": [
              "null",
              "string"
            ]
          },
          {
            "default": null,
            "name": "source",
            "type": [
              "null",
              "string"
            ]
          },
          {
            "default": null,
            "name": "point_id",
            "type": [
              "null",
              "string"
            ]
          },
          {
            "default": null,
            "name": "geography",
            "type": [
              "null",
              "string"
            ]
          },
          {
            "default": null,
            "name": "neighborhood",
            "type": [
              "null",
              "string"
            ]
          }
        ],
        "name": "Geo",
        "type": "record"
      }
    }
  ],
  "name": "Avro",
  "type": "record"
}
