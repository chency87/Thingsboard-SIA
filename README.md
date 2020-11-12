# Thingsboard-SIA



Beckend of IoT paltform, which is based on [Thingsboard](http://www.thingsboard.io). 

1. Deleted the UI、Dcker、k8s、msa modules. For more information, please visit [Thingsboard](http://www.thingsboard.io). 

2. Added Access Control Module based ABAC and Rule Engine.

   - Add rule

   ```{'siasec2@sia.org','testtenant@thingsboard.org','siasec3@sia.org'}.contains(sub.email)```

   ```sub.id=='45de5c00-ecce-11ea-b99b-e15d85b1588d' && sub.email=='siasec@sia.org'```

3. Added some data collection plugins for different protocols.

### Clone

```git clone git@github.com:chency87/Thingsboard-SIA.git```

### Compile

``` mvn clean install -DskipTests```


