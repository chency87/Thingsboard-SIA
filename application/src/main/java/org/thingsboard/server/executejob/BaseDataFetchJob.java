package org.thingsboard.server.executejob;

import cn.sia.sec.proto.plugin.AsyncDataFetchPluginService;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import org.thingsboard.server.common.transport.TransportContext;
import org.thingsboard.server.common.transport.TransportService;
import org.thingsboard.server.common.transport.TransportServiceCallback;
import org.thingsboard.server.common.transport.adaptor.JsonConverter;
import org.thingsboard.server.controller.idscontroller.ConstantConfValue;
import org.thingsboard.server.gen.transport.TransportProtos;
import org.thingsboard.server.plugin.bean.DataFetchPlugin;
import org.thingsboard.server.transport.http.DeviceApiController;
import org.thingsboard.server.gen.transport.TransportProtos.SessionInfoProto;
import org.thingsboard.server.transport.http.HttpTransportContext;
import org.thingsboard.server.gen.transport.TransportProtos.ValidateDeviceCredentialsResponseMsg;
import org.thingsboard.server.gen.transport.TransportProtos.DeviceInfoProto;
import org.thingsboard.server.gen.transport.TransportProtos.ValidateDeviceTokenRequestMsg;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
@Service
public class BaseDataFetchJob implements BaseJob {
    @Autowired
    private HttpTransportContext transportContext;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String jarPath = jobDataMap.get(ConstantConfValue.dataFetchJobDataParamJarPath).toString();
        String className = jobDataMap.get(ConstantConfValue.dataFetchJobDataParamClassName).toString();
        String deviceToken = jobDataMap.getString(ConstantConfValue.dataFetchJobDataParamToken);

        Map<String,String> configs = new HashMap<>();
        jobDataMap.remove(ConstantConfValue.dataFetchJobDataParamJarPath);
        jobDataMap.remove(ConstantConfValue.dataFetchJobDataParamClassName);
        jobDataMap.remove(ConstantConfValue.dataFetchJobDataParamToken);
        for(String key : jobDataMap.keySet()){
            configs.put(key,jobDataMap.get(key).toString());
        }
//        Map<String,String> jobData = new HashMap<String,String>();
//        jobData.put("port","502");
//        jobData.put("ip","127.0.0.1");
//        jobData.put("funCode","1");
//        jobData.put("slaveId","1");
//        jobData.put("address","0");
//        jobData.put("quantity","4");
//        jobData.put("attr","od,op,dp,di");
//        jobData.put("className",plugin.getClassName());

        File jarFile = new File(jarPath);
        URL url = null;
        try {
            url = jarFile.toURI().toURL();
            ClassLoader system = new URLClassLoader(new URL[] { url }, Thread.currentThread().getContextClassLoader());
            Class<?> cs = system.loadClass(className);
            AsyncDataFetchPluginService service = (AsyncDataFetchPluginService)cs.newInstance();//dfm.getInstanceByClassName(plugin.getClassName());
            Map<String, String> map = service.executeAsyncService(configs);
            if(map.get("status") =="true"){
                String  json = map.get("data");
//                System.out.println(json);
                DeferredResult<ResponseEntity> responseWriter = new DeferredResult<ResponseEntity>();
                transportContext.getTransportService().process(ValidateDeviceTokenRequestMsg.newBuilder().setToken(deviceToken).build(),
                        new DeviceAuthCallback(transportContext, responseWriter, sessionInfo -> {
                            TransportService transportService = transportContext.getTransportService();
                            transportService.process(sessionInfo, JsonConverter.convertToTelemetryProto(new JsonParser().parse(json)),
                                    new HttpOkCallback(responseWriter));
                        }));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static class DeviceAuthCallback implements TransportServiceCallback<ValidateDeviceCredentialsResponseMsg> {
        private final TransportContext transportContext;
        private final DeferredResult<ResponseEntity> responseWriter;
        private final Consumer<SessionInfoProto> onSuccess;

        DeviceAuthCallback(TransportContext transportContext, DeferredResult<ResponseEntity> responseWriter, Consumer<TransportProtos.SessionInfoProto> onSuccess) {
            this.transportContext = transportContext;
            this.responseWriter = responseWriter;
            this.onSuccess = onSuccess;
        }

        @Override
        public void onSuccess(ValidateDeviceCredentialsResponseMsg msg) {
            if (msg.hasDeviceInfo()) {
                UUID sessionId = UUID.randomUUID();
                DeviceInfoProto deviceInfoProto = msg.getDeviceInfo();
                SessionInfoProto sessionInfo = SessionInfoProto.newBuilder()
                        .setNodeId(transportContext.getNodeId())
                        .setTenantIdMSB(deviceInfoProto.getTenantIdMSB())
                        .setTenantIdLSB(deviceInfoProto.getTenantIdLSB())
                        .setDeviceIdMSB(deviceInfoProto.getDeviceIdMSB())
                        .setDeviceIdLSB(deviceInfoProto.getDeviceIdLSB())
                        .setSessionIdMSB(sessionId.getMostSignificantBits())
                        .setSessionIdLSB(sessionId.getLeastSignificantBits())
                        .build();
                onSuccess.accept(sessionInfo);
            } else {
                responseWriter.setResult(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
            }
        }

        @Override
        public void onError(Throwable e) {
            log.warn("Failed to process request", e);
            responseWriter.setResult(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    private static class HttpOkCallback implements TransportServiceCallback<Void> {
        private final DeferredResult<ResponseEntity> responseWriter;

        public HttpOkCallback(DeferredResult<ResponseEntity> responseWriter) {
            this.responseWriter = responseWriter;
        }

        @Override
        public void onSuccess(Void msg) {
            responseWriter.setResult(new ResponseEntity<>(HttpStatus.OK));
        }

        @Override
        public void onError(Throwable e) {
            responseWriter.setResult(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
