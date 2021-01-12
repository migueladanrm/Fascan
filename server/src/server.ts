import express, { Application, NextFunction, Request, Response } from "express";
import bodyParser from "body-parser";
import morgan from "morgan";
import * as http from "http";
import { DetectorRoute } from "./routes/detector.route";
import { authenticateRequest } from "./middlewares/authentication.middleware";
export class Server {
  private app: Application;
  private port = process.env.SERVER_PORT || 8000;

  constructor() {
    this.setupApp();
    this.setupServer();
  }

  private setupApp(): void {
    this.app = express()
      .use(bodyParser.json())
      .use(morgan("dev"))
      .use((req: Request, res: Response, next: NextFunction) => {
        res
          .header("Access-Control-Allow-Origin", "*")
          .header(
            "Access-Control-Allow-Methods",
            "GET, POST, PATCH, PUT, DELETE, OPTIONS"
          )
          .header(
            "Access-Control-Allow-Headers",
            "Origin, X-Requested-With, Content-Type, Accept, Authorization, Access-Control-Allow-Credentials"
          )
          .header("Access-Control-Allow-Credentials", "true");
        next();
      })
      .use(authenticateRequest)
      .get("/", (req: Request, res: Response) => {
        res.send("<h1>Hello, world!</h1>");
      })
      .use("/detector", DetectorRoute(null));
  }

  private setupServer(): void {
    http
      .createServer(this.app)
      .listen(this.port)
      .on("listening", (): void => {
        console.log(`Server listening on port ${this.port}`);
      })
      .on("error", (error: NodeJS.ErrnoException): void => {
        if (error.syscall !== "listen") throw error;

        let bind =
          typeof this.port === "string"
            ? "Pipe " + this.port
            : "Port " + this.port;

        switch (error.code) {
          case "EACCES":
            console.error(`${bind} requires elevated privileges`);
            process.exit(1);
          case "EADDRINUSE":
            console.error(`${bind} is already in use`);
            process.exit(1);
          default:
            throw error;
        }
      });
  }
}
