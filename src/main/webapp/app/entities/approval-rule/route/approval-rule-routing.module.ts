import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ApprovalRuleComponent } from '../list/approval-rule.component';
import { ApprovalRuleDetailComponent } from '../detail/approval-rule-detail.component';
import { ApprovalRuleUpdateComponent } from '../update/approval-rule-update.component';
import { ApprovalRuleRoutingResolveService } from './approval-rule-routing-resolve.service';

const approvalRuleRoute: Routes = [
  {
    path: '',
    component: ApprovalRuleComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApprovalRuleDetailComponent,
    resolve: {
      approvalRule: ApprovalRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApprovalRuleUpdateComponent,
    resolve: {
      approvalRule: ApprovalRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApprovalRuleUpdateComponent,
    resolve: {
      approvalRule: ApprovalRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(approvalRuleRoute)],
  exports: [RouterModule],
})
export class ApprovalRuleRoutingModule {}
