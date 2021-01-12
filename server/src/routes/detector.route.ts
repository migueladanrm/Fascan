import { Request, Response, Router } from "express";
import { DetectorService } from "../services/detector.service";

export function DetectorRoute(service: DetectorService): Router {
  return Router().post("/", async (req: Request, res: Response) => {});
}
